package practice.RxJava.blocking;

import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import practice.RxJava.data.Car;
import practice.RxJava.data.CarMaker;
import practice.RxJava.data.SampleObservable;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BlockingTest {

        /** blockingFirst **/
        @Test
        public void get_car_stream_first_test() throws Exception {
            //when
            Car car = SampleObservable.getCarStream().blockingFirst();
            String result = car.getCarName();
            //then
            assertThat(result).isEqualTo("말리부");
        }

        @Test
        public void get_sales_of_branchA_first_test() throws Exception {
            //when
            Integer result = SampleObservable.getSalesOfBranchA().take(6).blockingFirst();
            //then
            assertThat(result).isEqualTo(15_000_000);
        }

        /** blockingLast **/
        @Test
        public void blocking_last_only_after_complete() throws Exception {
            //when
            Car car = SampleObservable.getCarStream().blockingLast();
            String result = car.getCarName();
            //then
            assertThat(result).isEqualTo("SM5");
        }

        @Test
        public void blocking_last_test_two() throws Exception {
            //when
            Integer result = SampleObservable.getSalesOfBranchA().take(6).blockingLast();
            //then
            assertThat(result).isEqualTo(40_000_000);
        }

        /** blockingSingle **/
        @Test
        public void blockingSingle_test_one() throws Exception {
            //when
            Integer result = SampleObservable.getSalesOfBranchA()
                                             .filter(sales -> sales > 30_000_000)
                                             .take(1)
                                             .blockingSingle();
            //then
            assertThat(result).isEqualTo(35_000_000);
        }

        @Test()
        public void blockingSingle_test_two() {
            assertThrows(RuntimeException.class,
                         BlockingTest::get_multiple_values_with_blockingSingle);
        }

        static void get_multiple_values_with_blockingSingle() {
            SampleObservable.getSalesOfBranchA().filter(sales -> sales > 30_000_000).take(2).blockingSingle();
        }

        @Test
        public void blockingGet_Empty() throws Exception {
            Object obj = Observable.empty().firstElement().blockingGet();
            assertThat(obj).isNull();
        }

        @Test
        public void blockingGet_with_reduce() throws Exception {
            Integer sales = SampleObservable.getSalesOfBranchA()
                    .reduce((a, b) -> a + b)
                    .blockingGet();

            assertThat(sales).isEqualTo(326000000);
        }

        @Test
        public void blockingGet_with_zip() throws Exception {
            Integer sales = Observable.zip(
                    SampleObservable.getSalesOfBranchA(),
                    SampleObservable.getSalesOfBranchB(),
                    SampleObservable.getSalesOfBranchC(),
                    (a, b, c) -> a + b + c
            )
            .doOnEach(data -> System.out.println("data " + data))
            .reduce(Integer::sum)
            .blockingGet();

            assertThat(sales).isEqualTo(992000000);
        }

        /*
        * blockingIterable:: apply for a various condition checking
        * blockingForEach:: apply for a same condition checking on every case
        * */
        @Test
        public void blockingIterable_Loop() throws Exception {
            Iterable<CarMaker> carMakers = SampleObservable.getCarMakerStream()
                                                           .blockingIterable();
            Iterator<CarMaker> carMakerIterator = carMakers.iterator();

            assertThat(carMakerIterator.next()).isEqualTo(CarMaker.CHEVROLET);
            assertThat(carMakerIterator.next()).isEqualTo(CarMaker.HYUNDAE);
            assertThat(carMakerIterator.next()).isEqualTo(CarMaker.SAMSUNG);
        }

        @Test
        public void blockingForEach_Loop() throws Exception {
            SampleObservable.getSpeedOfSectionA()
                    .filter(speed -> speed > 110)
                    .blockingForEach(speed -> assertThat(speed).isGreaterThan(110));
        }
        
        @Test
        public void blockingSubscribe() throws Exception {
            SampleObservable.getSalesOfBranchA()
                            .blockingSubscribe(SumAll::sumValue);

            assertThat(SumAll.value).isEqualTo(326000000);
        }

        static class SumAll {
            static int value = 0;

            static int sumValue(int input) {
                return value += input;
            }
        }
}

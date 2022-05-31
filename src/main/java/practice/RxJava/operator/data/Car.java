package practice.RxJava.operator.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    private CarMaker carMaker;
    private String carName;
    private CarType carType;
    private int carPrice;
    private boolean isNew;

    public Car(CarMaker carMaker, String carName, CarType carType, int carPrice) {
        this.carMaker = carMaker;
        this.carName = carName;
        this.carType = carType;
        this.carPrice = carPrice;
    }
}

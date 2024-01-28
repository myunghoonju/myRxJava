#### Jan 23rd
- 동시성과 병렬성
  - 동시성은 cpu가 한번에 많은 일을 처리하는 것에 중점.
      - 동시성은 작업의 빠르기가 목적이 아닌 효율적인 cpu 사용에 더 중점이 있다.
      - 동시성은 작업목록이 cpu core 보다 많을 경우 해당.
    - 병렬성은 cpu가 동시에 많은 일을 수행하는 것에 중점(cpu가 빠르게 동작).
      - 단일 코어에서는 병렬성이 구현될 수 없다.
      - 병렬성은 동시성의 하위 개념으로 작업을 여러 thread 분리, cpu에 분배하여 동시 실행
      - 병렬성은 작업목록이 cpu 코어 수 보다 같거나 적을 경우 효율적 
#### Jan 24th
- Context switch
  - 프로세스의 정보이며 PCB(Process Control Block)에 저장된다.
  - CPU가 프로세스 간 PCB의 정보를 교체하고 캐시를 비우는 일련의 과정
  - TCB(Thread Control Block)은 thread 상태정보를 저장하는 자료구조
  - Thread 당 PCB 내에서 TCB 생성
  - context switch 는 Process 간에 또는 Thread 간에 일어나며 동작 방식은 유사하다
- CPU bound & I/O bound
  - 프로세스는 cpu 작업, i/o 작업의 연속된 흐름으로 진행
  - Burst: 한 작업을 짧은 시간동안 집중적으로 처리/실행 하는 구간과 시간을 의미
    - 프로세스마다 cpu burst와 i/o burst가 차지하는 비율이 다르다.
    - 이 비율을 기준으로 각각의 바운드 프로세스를 정한다
- Thread
  - 사용자 수준과 커널 수준의 스레드로 구분된다.
  - CPU는 OS스케쥴러가 예약하는 커널 스레드만 할당받아 실행시킨다.
  - 사용자 수준 스레드는 커널 수준 스레드와의 매핑이 필요하다.
#### Jan 28th
- Thread creation
  - platform thread: 운영체제 스케줄러에 의해서 생성된 커널 스레드에 1:1 매핑된 스레드
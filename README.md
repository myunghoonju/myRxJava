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
#### Jan 29th
- Thread State
  - 객체 생성 - NEW
    - 스레드 객체가 생성됨, 아직 시작되지 않은 스레드 상태
  - 실행 대기 - RUNNABLE
    - 실행 중이거나 실행 가능한 스레드 상태
  - 일시정지 - WAITING
    - 대기 중인 스레드 상태로서 다른 스레드가 특정 작업을 수행하기를 기다림(시간제한 없음)
  - 일시정지 - TIMED_WAITING
    - 대기 시간이 지정된 스레드 상태로서 다른 스레드가 특정 작업을 수행하기를 기다림
  - 일시정지 - BLOCKED
    - 모니터 락(lock)이 해제될 때 까지 기다리며 차단된 스레드 상태
  - 종료 - TERMINATED
    - 실행이 완료된 스레드 상태
#### Feb 1st
- sleep()
  - OS 스케줄러가 현재 스레드를 지정된 시간 동안 대기 상태로 전환하고   
    다른 스레드 혹은 프로세스에게 CPU 사용하도록 함  
  - 대기 시간이 끝나면 스레드 상태는 실행상태가 아닌 대기 상태로 전환, CPU 실행까지 기다린다  
  - 실행 상태가 되면 스레드는 남은 지점부터 실행 시작
  - 동기화 메서드 영역에서 수면 중 스레드는 획득한 모니터 혹은 락을 잃지 않고 유지
  - 슬립 중에 인터럽트 발생 시 현재 스레드는 대기에서 해제되고 실행상태로 전환 및 예외처리
  - 스레드 수면 시간은 OS 스케줄러 및 시스템 기능에 따라 제한되기 때문에 정확성이 보장되지 않으며  
    시스템의 부하가 많고 적음에 따라 지정한 수면 시간과 차이 발생 가능  

#### Feb 26th
- 리액티브 시스템 설계원칙
  - 응답성(responsive)
  - 회복성(resilient)
  - 탄력성(elastic)
  - 메시지 기반(message driven)  
- 리액티브 프로그래밍
  - 데이터 소스에 변경이 있을때마다 데이터 전파
  - 선언형 프로그래밍 패러다임
    - 함수형 프로그래밍 기법
  - 리액티브 스트림즈
    - publisher
    - subscriber
    - subscription
    - processor
    - etc...,
    - 구현체
      - rxJava, reactor, java 9's flow api, etc...,
- 리액터 용어
  - publisher
  - subscriber
  - emit
  - sequence
    - publisher가 emit하는 데이터의 연속적인 흐름
  - subscribe <-> dispose
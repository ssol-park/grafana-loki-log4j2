## Ref
#### tjahzi/log4j2-appender
- https://github.com/tkowalcz/tjahzi/blob/master/log4j2-appender/README.md
#### Grafana Learn
- https://grafana.com/docs/grafana/latest/
#### LogQL query
- https://grafana.com/docs/loki/latest/query/bp-query/
#### Prometheus
- https://docs.micrometer.io/micrometer/reference/implementations/prometheus.html
#### Grafana dashboard
 - https://grafana.com/grafana/dashboards/4701-jvm-micrometer/

---
## Log4j2의 MDC 데이터 흐름 및 동작 방식

### MDC 
- 로깅 프레임워크에서 제공 하며, 로깅 이벤트에 추가적인 컨텍스트 정보를 포함시키기 위해 사용된다 
- 주로 Java 로깅 프레임워크(예: Logback, Log4j)에서 사용되며, 특정 요청이나 스레드에 대한 부가 정보를 로그 메시지와 함께 기록하는 데 유용하다.

주요 개념
- MDC는 스레드 로컬(ThreadLocal) 저장소를 사용하여 데이터를 관리
- 데이터를 키-값 쌍으로 저장하여, 각 로그 메시지와 함께 해당 데이터를 출력할 수 있다.
- 동일 스레드 내에서는 해당 데이터가 유지되지만, 다른 스레드에서는 접근할 수 없다.

---

### MDC 데이터가 `logger`에 전달되는 과정

### **1. 로깅 호출 시 (`logger.info`)**
- `logger.info()` 호출 시 **`LogEvent` 객체**가 생성
- `LogEvent` 객체는 호출 당시의 **MDC 복사본**을 저장
- **MDC 데이터는 스레드 로컬의 복사본**이므로, 이후 `MDC`를 수정해도 이미 생성된 `LogEvent`에는 반영되지 않는다.

---

### **2. `LogEvent` 객체의 MDC 데이터 전달**
- `LogEvent` 객체는 **Appender**로 전달되며, 이 객체의 MDC 데이터(복사본)가 로그 포맷팅 시 사용된다.

---

### **3. Custom Filter/Converter 동작 시점 (중요)**
- **Custom Filter/Converter**는 `LogEvent`가 `Appender`로 전달된 이후 동작
- 따라서, **이미 복사된 MDC 데이터**만 접근 가능하고, 해당 시점에 MDC를 수정할 수 없다.

---

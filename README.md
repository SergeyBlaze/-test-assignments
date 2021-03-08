# **IP-Addr-Counter**

Пример запуска подсчета уникальных IP аддрессов
bufferSize - не обязательный параметр. Значение по умолчанию равно 2^20.
std::bufferSize =  = 1048576
```
gradlew IPCalcV1 -PfilePath="D:\addresses.txt" -PbufferSize=1048576
gradlew IPCalcV2 -PfilePath="D:\addresses.txt" -PbufferSize=1048576
```
IPCalcV1 - Версия последовательного чтения<br/>
IPCalcV2 - Версия в 2 потока чтения и подсчета

# **ThreadLocal**
Пример запуска
```
gradlew ThreadLocalRun
```
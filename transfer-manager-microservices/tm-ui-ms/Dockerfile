#Задаем базовый образ
FROM openjdk:11
WORKDIR /
#Копируем рабочую директорию проекта в Docker для коректного запуска
COPY ./ ./
#Задаем переменную для передачи в Docker во время сборки образа
ADD target/*.jar app.jar
#EXPOSE 8090
#Команда с аргументами для вызова во время выполнения контейнера
CMD java -jar -Dspring.profiles.active=prod app.jar

# Simple Kafka для демонстрации базовых возможностей Kafka

Используем, Kafka, Kafka-UI, Postgres, Swagger

# 2 микра:
MicroOne потребитель (consumer)
принимает пользователей и сообщения из кафки и пишет их в базу

MicroTwo продюсер (producer)
принимает пользователей и сообщения через телеграм или по ресту и отправляет в кафку

Kafka-UI крутится на http://localhost:8090
# Запуск
Запустить docker-compose запустить MicroOne MicroTwo

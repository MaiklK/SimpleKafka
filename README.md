# Simple Kafka для демонстрации базовых возможностей Kafka

Используем, Kafka, Kafka-UI, Postgres, Swagger

# 2 микра:
MicroOne потребитель (consumer)
принимает сообщения из кафки и пишет их в базу

MicroTwo продюсер (producer)
принимает сообщения из телеграм бота https://t.me/JavaEmojiBot или по ресту и отправляет в кафку, данные о пользователе и само сообщение

Kafka-UI крутится на http://localhost:8090

Swagger крутится на http://localhost:8082/swagger-ui/index.html
# Запуск
Запустить docker-compose запустить MicroOne MicroTwo

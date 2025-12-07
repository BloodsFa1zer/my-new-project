#!/bin/bash
cd "$(dirname "$0")"
rm -rf data/

# Тестуємо інтерактивний режим з різними командами
(
echo "1"           # Вибираємо інтерактивний режим
sleep 1
echo "4"           # Переглянути всі банки
sleep 1
echo ""            # Press Enter
sleep 1
echo "3"           # Переглянути всі кредити
sleep 1
echo ""            # Press Enter
sleep 1
echo "5"           # Пошук за типом
sleep 1
echo "MORTGAGE"    # Тип кредиту
sleep 1
echo ""            # Press Enter
sleep 1
echo "6"           # Пошук за банком
sleep 1
echo "PrivatBank"  # Назва банку
sleep 1
echo ""            # Press Enter
sleep 1
echo "7"           # Найнижча процентна ставка
sleep 1
echo "100000"      # Мінімальна сума
sleep 1
echo "500000"      # Максимальна сума
sleep 1
echo ""            # Press Enter
sleep 1
echo "0"           # Вихід
sleep 1
) | mvn exec:java -Dexec.mainClass="credit.Application" -q 2>&1

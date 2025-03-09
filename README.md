Приложение состоит из 2 экранов, на 1-ом из которых отображается список новостей, которые возможно скрывать. Так же реализован поиск среди новостей.
На втором экране происходит отображение полной версии новости.
Структура приложения разделена на 3 слоя: 
1. Слой data : Содержит базу данных Room: сущность, dao, и объект базы данных. Содержит репозиторий, через который реализовано получние данных из базы данных и с сервера, так же в репозитории реализована обработка данных: обновление списка новостей: изменение статуса игнорирования
у выбранных пользователем новостей и последующая фильтрация новостей по игнорируемым, так же исправление ошибки в url по полю mobileUrl. В ходе тестирования была обнаружена ошибка: после хоста и порта в url не хватает строки 'api/, после которой идёт путь, исправление
реализовано при помощи регулярных выражений.
2. Слой domain : Содержит сущность, в которой представлены все возможные экраны для навигации, так же содержит сущность ApiResponse, по которой обеспечивается корректное получение данных с Api.
3. Слой ui : Содержит экраны и вью моделю, взаимодействующую с репозиторием для получения данных. Для того, чтобы на экране отображения списка новостей обновлялись свежие данные. в вью модели данные представлены в виде StateFlow, собираемые в View.
Так же в данном слое представлен Навигационный граф, в котором описаны пути, по которым работает навигация в приложении. Для корректного перехода от экрана с списком новостей к webView реализовано закодирование MobileUrl и последующая раскодировка для использования
в webView. Для отображения изображений использована библиотека Glide, которая имеет Compose версию.
В приложении реализована инъекция зависимостей, что позволяет уменьшить зависимости между различными частями приложения. Для инъекции зависимостей используется библиотека Dagger2.
![image](https://github.com/user-attachments/assets/c59ca1ae-6cfc-4427-8fe1-f6d48d43e3e8)

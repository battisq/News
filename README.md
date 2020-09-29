# News
Это приложение представляет собой новострую ленту основанную за сервисе https://newsapi.org/

## Используемые технологии
Стек:
+ Koin;
+ Retrofit;
+ Architecture Components (Room + ViewModel + PagedList + Navigation + ViewBinding);
+ Picasso.

Архитектурный паттерн:
+ MVVM.

## Реализовано следующее
+ Компонент Material CardView для элементов в списке;
+ Material Design Floating Action Button для кнопки прокрутки вверх;
+ Material Design Extended Floating Action Button для кнопки повторной попытки загрузки данных (появляется, если данные не загрузились из-за отсутствия интернета);
+ SwipeRefreshLayout для ручного обновления данных;
+ Выгрузка из NewApi в базу данных Room, а оттуда вывод на экран (данные хранятся в локальном хранилище);
+ Пагинированный список с префечингом в 5 элементов;
+ Новость открывается в WebView;
+ Приложения построено на одном Activity и трёх Fragment.

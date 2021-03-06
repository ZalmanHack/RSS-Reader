# RSS-Reader

Данное приложение позволяет одновременно просматривать, выбранные Вами, новостные ленты в формате **RSS** *(Rich Site Summary)*.

## Функционал
* режим работы офлайн;
* автоматическое обновление новостной ленты при входе в приложение;
* ручное обновление пользовательской новостной ленты с помощью жеста сверху вниз;
* валидация новостных лент и ссылок на них при добавлении в приложение;
* переключение между новостными лентами каналов с помощью жестов вправо или влево, а также с помощью вкладок;
* чтение полного текста новости;
* адаптивное представление карточек новостных лент в зависимости от ориентации экрана;
* добавление и удаление новостных каналов;
* получение ссылок на первоисточник;
* открытие новостей в браузере;
* копирование текста и заголовка новости.

## Архитектура
### Fragments
* `FeedsPageFragment.java` - главный фрагмент приложения, отображающий новостные ленты с помощью `ViewPager2` и `TabLayout`. Позволяет:
    * просматривать одновременно большое количество новостных каналов;
    * адаптивно отображать информацию в портретной и ландшафтной ориентации;
    * ориентироваться по новостным лентам с помощью жестов и вкладок;
* `FeedFragment.java` - фрагмент, отображающий новостную ленту одного канала;     
* `SettingsFragment.java` - фрагмент настроек приложения, позволяющий пользователю добавлять и удалять новостные ленты. Доступен из панели инструментов в `FeedsPageFragment`. Позволяет:
    * добавлять новостной RSS канал;
    * удалять новостной канал;
    * автоматически определять название канала;
    * проверять и уведомлять о возможности добавления канала;
* `ShowItemFragment.java` - фрагмент для отображения полной информации по выбранной ранее новости. Доступен по нажатию на одну из карточек новостных лент. Позволяет:
    * название канала;
    * название новости;
    * изображение новости *(при наличии)*;
    * автора новости *(при наличии)*;
    * дату публикации *(при наличии)*;
    * текст новости *(при наличии)*;
    * категории, относящиеся к новости *(при наличии)*;
    * возможность копирование текста новости;
    * возможность поделиться ссылкой на новость;
    * возможность открыть страницу новости в браузере.

### Adapters
* `BindingAdapters.java` - адаптер, расширяющий возможности `ImageView`. Позволяет загружать изображения по ссылке;
* `CategoryAdapter.java` - адаптер, визуализирующий категории выбранной новости в виде небольших плашек, расположенных друг за другом;
* `ChannelAdapter.java` - адаптер, визуализирующий добавленные каналы. Также он позволяет удалить ненужные каналы;
* `FeedAdapter.java` - адаптер для отображения новостной ленты одного канала. Адаптирован под портретную и ландшафтную ориентацию;
* `FeedsPageAdapter.java` - адаптер для отображения новостных лент типа `FeedFragment`;
* *`OnClickChannelInterface.java`* - интерфейс для обработки нажатий по списку из `Channel.java`;
* *`OnClickItemInterface.java`* - интерфейс для обработки нажатий по списку из `Item.java`;

### Databases
![Database](/screenshots/db.png)
* Entities
    * `Category.java` - сущность для хранения всех категорий новостных лент. Производит индексирование по всем полям с требованием уникальности;
    * `Channel.java` - сущность для хранения данных о новостных каналах. Производит индексирование по всем полям с требованием уникальности;
    * `Item.java` - сущность для хранения полной информации по одной новостной публикации. Производит индексирование по всем полям с требованием уникальности, а также каскадное удаление;
    * `ItemWithCategory.java` - промежуточная сущность, связывающая по ключу данные `Item` и `Category`;
* `ChannelWithItems.java` - запрос для получения всей новостной ленты по определенному каналу, содержащий в себе данные о канале и новостную ленту в виде списка;     
* `Converters.java` - конвертер для преобразований типа `Date` в тип `Long` и обратно;     
* `ItemWithChannelAndCategories.java` - запрос для получения всей новостной ленты с категориями по определенному каналу в виде списка;    
* *`AppDatabase.java`* - абстрактный класс для работы с базой данных приложения;
* *`ChannelsFeedDao.java`* - абстрактный класс, в котором описаны все необходимые запросы к базе данных; 

### Helpers
* `CustomDrawableCompat.java` - класс для окраски векторных изображений и иконок с поддержкой старого api; 

### Network
* `RssParser.java` - парсер новостной RSS ленты;
* `RetrofitService.java` - сервис для получения новостный лент с помощью REST API;
* *`RetrofitServiceNewsInterface.java`* - интерфейс запросов.

### Repository
* `AppRepo.java` - репозиторий, предоставляющий доступом к базе данных приложения и внешнему информационному ресурсу для обновления базы данных;

### ViewModels
* `FeedViewModel.java` `SettingsViewModel.java` - архитектурные решения, основанные на паттерне проектирования MVVM, для построения логики приложения.

### Navigation
* `main_graph.xml` - граф взаимосвязей представлений интерфейса.

### Dependency Injections (DI)
Паттерн для создания внешних зависимостей. Создает внешние зависимости для:
* `AppRepo`
* `AppDatabase`
* `OkHttpClient`
* `Retrofit`
* `RssParser`

### Screenshots
<p float="left">
  <img src="/screenshots/s1.png" width="300" />
  <img src="/screenshots/s2.png" width="300" /> 
  <img src="/screenshots/s3.png" width="300" />
</p>
<p float="left">
  <img src="/screenshots/s6.png" width="900" />
</p>
<p float="left">
  <img src="/screenshots/s4.png" width="300" />
  <img src="/screenshots/s7.png" width="300" /> 
  <img src="/screenshots/s8.png" width="300" />
</p>


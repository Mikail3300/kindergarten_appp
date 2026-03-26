# Учет детей в садике — Android Studio (Java)

## Тема
Android-приложение для учета детей в садике: дети, родители, группы и воспитательницы.

## Краткое описание
Приложение позволяет:
- добавлять записи о детях;
- просматривать список воспитанников;
- редактировать и удалять записи;
- искать детей по имени, родителю, группе и воспитательнице;
- открывать детальную карточку ребенка;
- экспортировать список в текстовый файл во внешнее хранилище приложения.

## Используемые технологии
- **Java**
- **Android Studio**
- **SQLite / SQLiteOpenHelper**
- **RecyclerView**
- **Parcelable**
- **Intent + Activity Result API**
- **AlertDialog / DialogFragment**
- **Toast**
- **Snackbar**
- **DatePickerDialog / TimePickerDialog**
- **Spinner**
- **AutoCompleteTextView**
- **CheckBox**
- **ToggleButton**
- **RadioButton**
- **SeekBar**
- **Menu / Submenu**
- **XML-анимации**

## Реализованные требования из задания
### 1. Экраны (Activity)
- `MainActivity` — список детей, поиск, меню, экспорт.
- `AddEditChildActivity` — форма добавления и редактирования.
- `ChildDetailActivity` — детальная карточка ребенка.

### 2. Передача данных между Activity
- Передача объекта `Child` через `Intent`.
- Использован `Parcelable`.
- Получение результата через `ActivityResultLauncher`.

### 3. Пользовательский интерфейс
В проекте используются:
- `TextView`
- `EditText`
- `Button`
- `ImageView`
- `RecyclerView`
- `CheckBox`
- `ToggleButton`
- `RadioButton`
- `Spinner`
- `AutoCompleteTextView`
- `SeekBar`
- `Menu`

### 4. Работа с базой данных
- SQLite база `kindergarten.db`
- таблица `children`
- операции:
  - добавление
  - чтение
  - изменение
  - удаление
  - поиск

### 5. Взаимодействие с пользователем
- `Toast` — проверка обязательных полей
- `Snackbar` — сохранение, удаление, экспорт
- `AlertDialog` / `DialogFragment` — подтверждение удаления

### 6. Анимация
- анимация кнопки сохранения;
- анимация иконки на главном экране;
- анимация элементов списка;
- анимация перехода между Activity.

## Структура проекта
- `app/src/main/java/.../model` — модель `Child`
- `app/src/main/java/.../data` — SQLite helper и repository
- `app/src/main/java/.../adapter` — адаптер RecyclerView
- `app/src/main/java/.../ui` — Activity и DialogFragment
- `app/src/main/res/layout` — XML-макеты
- `app/src/main/res/menu` — меню и подменю
- `app/src/main/res/anim` — анимации
- `screenshots` — примеры экранов приложения

## Скриншоты приложения
1. `screenshots/01_main_screen.png` — главный экран со списком детей и поиском.
2. `screenshots/02_add_edit_screen.png` — экран добавления/редактирования записи.
3. `screenshots/03_detail_screen.png` — детальная карточка ребенка.

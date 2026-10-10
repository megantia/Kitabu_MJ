# Kitabu

Kitabu is a native Android library book rental and reservation app. Users browse a catalogue, search it by title or author, reserve an available book, and then renew, return or cancel the reservation from a dashboard that shows return deadlines and days remaining.

The name is the Swahili word for "book". The app was built for an assignment at STADIO Higher Education, and its look is inspired by a khanga textile.


## Features

- **Catalogue:** a two-column grid of books, each card showing title, author, category and an availability badge (green *Available*, red *Unavailable*).
- **Live search:** filter the catalogue by title or author as you type.
- **Reserve pop-up:** tap an available book, enter your name, choose 7, 14 or 30 days, and see the return-by date before confirming.
- **Reservations dashboard:** active bookings with reservation date, return deadline and days remaining (or days overdue in red).
- **Lifecycle actions:** *Renew* extends the deadline, *Return* frees the book, and *Cancel* removes a pending reservation.
- **Reactive UI:** the catalogue badge changes the moment a book is reserved or returned, with no manual refresh.
- **Offline first:** everything is stored in a local Room database, with no network needed.
- **Pre-populated data:** the catalogue is seeded with 16 prescribed textbooks from the STADIO BTech Web Design and Development 2024 booklist.

## Tech stack

| Area | Technology |
|---|---|
| Language | Kotlin 2.2.10 |
| UI | Jetpack Compose with Material 3 (Compose BOM 2026.02.01), no XML layouts |
| Navigation | Navigation Compose 2.9.7, single activity |
| Persistence | Room 2.8.5 (SQLite), annotation processing with KSP 2.3.11 |
| Async and state | Kotlin Coroutines 1.11.0, Flow and StateFlow |
| Lifecycle | ViewModel Compose and `collectAsStateWithLifecycle()` (Lifecycle 2.11.0) |
| Build | Android Gradle Plugin 9.3.3, Gradle Kotlin DSL, version catalogue |
| SDK | minSdk 24, targetSdk 37, compileSdk 37 |

## Getting started

### Requirements

- Android Studio
- JDK 25, which is the JetBrains Runtime bundled with Android Studio, so no separate install is needed
- An Android emulator or a physical device running Android 7.0 (API 24) or newer

### Run it

1. Clone the repository:

   ```bash
   git clone https://github.com/megantia/Kitabu_MJ.git
   ```

2. Open the `Kitabu` folder in Android Studio and let Gradle sync finish.
3. Select an emulator or connected device and press **Run**.


### Resetting the sample data

The sample books are only inserted when the database is first created. To get them back after changing the seed list or the data, uninstall the app or clear its storage and run it again.

## Architecture

Kitabu follows MVVM with a repository between the ViewModels and the database. Data flows up as `Flow` from Room, through the repository and ViewModels as `StateFlow`, and into Compose, which collects it with lifecycle awareness. User actions flow back down as `suspend` calls launched from `viewModelScope`, so no database work runs on the Main thread.

```
Compose screens  ->  ViewModels  ->  LibraryRepository  ->  DAOs  ->  Room (SQLite)
   (ui/)             (StateFlow)        (data/repository)    (data/local)
```

### Project structure

```
app/src/main/java/com/example/kitabu/
├── KitabuApplication.kt        Application class that builds the AppContainer
├── MainActivity.kt             Single activity hosting the navigation graph
├── data/
│   ├── local/                  Entities, DAOs, database, type converter, seed data
│   └── repository/             LibraryRepository (reserve, renew, return, cancel)
├── di/                         AppContainer (manual dependency injection)
├── domain/                     Days-remaining, date formatting, input validation
└── ui/
    ├── catalog/                Catalogue screen, ViewModel, book card, reserve dialog
    ├── dashboard/              Reservations screen, ViewModel, booking card
    ├── components/             Shared textile bands and dialog card
    ├── navigation/             NavHost, routes and bottom navigation bar
    └── theme/                  Colours, typography and Material theme
```

### Database

Two related tables, defined in `data/local`:

| `books` (BookEntity) | `bookings` (BookingEntity) |
|---|---|
| `bookId` (auto-generated primary key) | `bookingId` (auto-generated primary key) |
| `title`, `author`, `category` | `bookOwnerId` (foreign key to `books.bookId`, cascade delete) |
| `isAvailable` (default `true`) | `userName` |
| | `bookingDate`, `returnDeadline` (timestamps) |
| | `status` (`PENDING`, `ACTIVE`, `RETURNED`) |

`BookingStatus` is stored as text through a `TypeConverter`. `KitabuDatabase` is a thread-safe singleton built with `Room.databaseBuilder` that seeds the sample books on first creation and uses `fallbackToDestructiveMigration` during development.

### Reservation lifecycle

| Action | Effect on the booking | Effect on the book |
|---|---|---|
| Reserve | New booking with status `PENDING` | Marked unavailable |
| Renew | Deadline set to 7 days from today, status becomes `ACTIVE` | No change |
| Return | Status becomes `RETURNED` and the booking leaves the dashboard | Marked available |
| Cancel (pending only) | Booking row deleted | Marked available |

### CRUD coverage

| Operation | Where |
|---|---|
| Create | `BookDao.insertBook` and `insertBooks` (seeding), `BookingDao.insertBooking` (reserve) |
| Read | `getAllBooks`, `getAvailableBooks`, `searchBooks` (`LIKE`), and `getActiveBookingsWithBooks` (join) |
| Update | `BookingDao.renewBooking`, `markReturned`, and `BookDao.setAvailability` |
| Delete | `BookingDao.cancelBooking` |

### Validation

- The reserving name is cleaned (control characters removed, repeated spaces collapsed, 40 character limit) and an empty name is rejected with an inline error.
- The rental duration must be between 1 and 30 days.

### Dependency injection

Dependencies are wired by hand in `AppContainer` rather than with Hilt. The app only has one repository, so a manual container keeps the setup small and avoids another annotation processor on top of Room's KSP. ViewModels are created through `viewModelFactory` helpers in `ui/ViewModelFactory.kt`.

## Design

The interface uses a khanga-inspired palette. The search bar, reservations header, dialog titles and navigation bar sit on indigo bands with a gold rose border, and the rest of the screen is kept plain for legibility.

| Colour | Hex | Used for |
|---|---|---|
| Indigo | `#2D0080` | Bands, secondary text, outlines |
| Deep indigo | `#1A0050` | Primary text |
| Amber gold | `#FFBF03` | Rose border, selected tab, Cancel button, app icon background |
| Lavender | `#E1E2ED` | Page background |
| Green | `#50B14F` | Available badge, Renew and Return, Reserve |
| Red | `#DF0000` | Unavailable badge, overdue text |


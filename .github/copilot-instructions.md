# AI Coding Agent Instructions for MyProject Weather App

## Architecture Overview
This is a Spring Boot 3.2.2 application (Java 21) providing weather forecasting, user management, notifications, and trip planning services. Key components:

- **Layered Architecture**: Controllers → Services → Repositories/Mappers → Entities
- **Data Sources**: MySQL (JPA), Redis (caching), external APIs (OpenMeteo, Nominatim, TrackAsia, Gemini AI)
- **Core Features**: Multi-source weather data aggregation, user notifications, route planning, AI-powered risk analysis

## Key Patterns & Conventions

### API Response Structure
All endpoints return `ApiResponse<T>` wrapper with `code`, `result`, `message` fields. Success responses use code `1000`, errors use custom codes from `ErrorCode` enum.

```java
return ApiResponse.<List<CaiDatThongBaoResponse>>builder()
    .code(1000)
    .result(data)
    .message("Success message")
    .build();
```

### Exception Handling
- Custom `AppException` with `ErrorCode` enum (categorized: 1xxx users, 2xxx sources, 3xxx favorites, etc.)
- Global handler maps validation errors to enum keys
- Vietnamese error messages in `ErrorCode`

### Data Mapping
- Use MapStruct for entity ↔ DTO conversions
- Ignore auto-generated fields (`id`, timestamps) in create/update mappings
- `@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)` for partial updates

### Caching Strategy
- Redis with `StringRedisTemplate` for weather data (15min TTL)
- Cache keys follow pattern: function_model_lat_lon_days
- External API calls cached to reduce rate limits

### Validation & Security
- `@Valid` on request DTOs
- Spring Security configured (check `SecurityConfig`)
- Async operations enabled (`@EnableAsync`)
## Community & Content Moderation Patterns

### 1. Posting Workflow (Layer 1 Defense)
- **Flow**: User uploads photo -> System maps weather tags -> AI Check -> Save to DB.
- **AI Check**: `AnhCongDongService` MUST call `AiService` to scan text (`kiemDuyetVanBan`) and image (`kiemDuyetHinhAnh`).
  - If `REJECTED`: Set status `REJECTED`, notify user immediately via `ThongBaoService`.
  - If `APPROVED`: Set status `APPROVED` (visible).

### 2. Reporting Workflow (Layer 2 Defense)
- **Constraint**: One user can report a post only once (`baoCaoRepo.existsBy...`).
- **Auto-Hide Rule**: Logic in `AnhCongDongService`: If `soLuotBaoCao >= 20` -> Status changes to `PENDING` automatically (hide from feed).

### 3. Admin & Notifications
- **Admin Action**: `adminDuyetBai` updates status (`APPROVED`/`REJECTED`).
- **Notification**: MUST trigger `ThongBaoService.guiThongBaoHeThong()`:
  - If `REJECTED`: Send notification with specific reason.
  - If `APPROVED`: Send success notification.

### 4. Updated Code Organization for Community
- **Entities**:
  - `entity/Community`: `AnhCongDong` (Photo), `BinhLuan` (Comment), `LikeAnh`, `BaoCao` (Report).
  - `entity/Alert_Feedback`: `ThongBao` (Notification), `CaiDatThongBao`.
- **Controllers**: New endpoints under `/community` (e.g., `/community/posts`, `/community/report`).

## Development Workflows

### Build & Run
```bash
# Build
mvn clean install

# Run locally
mvn spring-boot:run
```

### Environment Setup
Required environment variables:
- `GEMINI_KEY`: For AI analysis
- `MAIL_USERNAME`/`MAIL_PASSWORD`: Gmail SMTP
- `TRACK_ASIA_KEY`: Route planning API

Database: MySQL on `localhost:3306/db_weather_app` (user: root, pass: root)
Redis: `localhost:6380`

### Testing
- Unit tests in `src/test/java`
- Integration tests for services with mocked external APIs
- Run with `mvn test`

## Code Organization
- **Entities**: `entity/` packages (weather, UserManagement, etc.)
- **DTOs**: `dto/request` and `dto/response` subpackages
- **Mappers**: MapStruct interfaces in `mapper/`
- **Services**: Business logic, external API integration
- **Controllers**: REST endpoints under `/identity` context path

## External Integrations
- **Weather**: OpenMeteo API with multi-source data entities
- **Geocoding**: Nominatim for address resolution
- **Routing**: TrackAsia for directions
- **AI**: Gemini 2.0 Flash via OpenAI-compatible endpoint for risk analysis

Reference key files:
- [MyprojectAppApplication.java](src/main/java/com/myproject/myproject_app/MyprojectAppApplication.java) - Main class
- [application.yaml](src/main/resources/application.yaml) - Configuration
- [GlobalExceptionHandler.java](src/main/java/com/myproject/myproject_app/exception/GlobalExceptionHandler.java) - Error handling
- [WeatherForecastService.java](src/main/java/com/myproject/myproject_app/service/WeatherForecastService.java) - Core service example</content>
<parameter name="filePath">d:\project_weather\myproject-app\.github\copilot-instructions.md
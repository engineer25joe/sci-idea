# BiasharaHub KE — Remote Management & User Activity Tracker (Django)

This is the remote management backend and interactive web tracking application for **BiasharaHub KE**.

## Key Features

1. **Remote Idea Management**:
   - Add, edit, or archive business ideas remotely.
   - Adjust capital ranges (KES), monthly profit projections, and accordion startup requirements without needing to release an app update.
2. **Success Stories & Testimonials**:
   - Add inspirational profiles of young Kenyan entrepreneurs.
3. **Live User Activity Tracking (Telemetry)**:
   - Tracks real-time user actions on the mobile and web app (`VIEW_IDEA`, `SEARCH`, `SAVE_IDEA`, `USE_CALCULATOR`, `COMPLETE_ONBOARDING`).
   - Interactive web dashboard shows top viewed ideas, recent event stream, and device IDs.
4. **Offline-First Synchronization**:
   - The Android application works 100% offline using its local database and caches telemetry events until a connection to the Django backend is available.

---

## Quickstart Instructions

### 1. Install Dependencies
```bash
cd backend_django
pip install -r requirements.txt
```

### 2. Apply Migrations & Seed Data
```bash
python manage.py makemigrations api
python manage.py migrate
python seed_data.py
```

### 3. Create Superuser (For Django Admin)
```bash
python manage.py createsuperuser
```

### 4. Run the Development Server
```bash
python manage.py runserver 0.0.0.0:8000
```

### 5. Access Interfaces
- **Interactive Tracking Web App**: `http://localhost:8000/` or `http://localhost:8000/dashboard/`
- **Django Admin Panel**: `http://localhost:8000/admin/`
- **REST API Endpoints**:
  - `GET /api/v1/ideas/`
  - `GET /api/v1/testimonials/`
  - `POST /api/v1/tracking/events/`

---

## Connecting the Android App
In the Android app:
1. Go to the **Profile** tab (Screen 10).
2. Tap **Settings & Django Backend**.
3. Set the URL (e.g. `http://10.0.2.2:8000` for Android Emulator or your public server URL).
4. Tap **Test Connection** & **Sync to Django Now**.

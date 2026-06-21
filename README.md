# AmeZakatInvestment - Zakat Gold Calculator

AmeZakatInvestment is a mobile application developed for estimating Zakat payments for gold based on two categories: **Gold Keeping** (Simpan) and **Gold Wearing** (Perhiasan).

## 🚀 Features

- **Real-time Calculation**: Results update automatically as you type weight and price or change the gold type.
- **Dual Categories**: Support for "Keep" (85g threshold) and "Wear" (200g threshold).
- **Comprehensive Results**:
    - **Total Gold Value**: The overall market value of your gold.
    - **Zakat Payable**: The value of gold that is subject to Zakat after subtracting the "Uruf" threshold.
    - **Total Zakat**: The final Zakat amount due (2.5% of the payable value).
- **Input Validation**: Handles empty fields and invalid inputs (e.g., negative numbers) gracefully with error messages.
- **Share Functionality**: Share the application URL directly from the ActionBar.
- **About Page**: Information about the developer and the application.

## 📐 Calculation Logic

The application follows the official Zakat calculation guidelines:

1.  **Total Value of Gold**: `Weight (g) * Current Gold Price (RM/g)`
2.  **Uruf Threshold (X)**:
    - **Keep**: 85 grams
    - **Wear**: 200 grams
3.  **Zakat Payable Value**: `(Weight - X) * Current Gold Price`
    - *Note: If Weight is less than or equal to X, Zakat Payable is RM 0.00.*
4.  **Total Zakat**: `2.5% * Zakat Payable Value`

## 📸 Screenshots

- **Splash Screen**: Branded entry point.
- **Main Calculator**: Interactive interface for calculations.
- **About Screen**: Developer details and project links.

## 🛠️ Built With

- **Java** - Programming language.
- **Android Studio** - Development environment.
- **Material Components** - For a modern UI/UX.

## 👤 Developer Information

- **Name**: Amier Zhafran
- **Student ID**: 2024353053
- **Course**: ICT602

## 📄 License

© 2026 AmeZakatInvestment. All rights reserved.

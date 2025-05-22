# 📱 Steel Count

**Steel Count** is an Android application that allows users to count and record items by capturing photos. It leverages AI-based image analysis to detect and autofill the count into a form.

---

## 🚀 Features

- 📸 Take photos or upload multiple images
- 🖼️ View selected images in a carousel
- 🧠 Auto-count steel items using AI (mock backend)
- 📝 Autofill the count into a form
- 📦 Images are zipped before submission
- ⏳ Loading indicators and smooth transitions
- ✅ Clean UI with form validation and feedback

---

## 🧑‍💻 Built With

- **Java** (Android SDK)
- **Android Studio**
- **MVVM Architecture**
- **LiveData & ViewModel**
- **Third-party libraries:** Glide, uCrop, Retrofit

---

## 📲 Installation & Run Instructions

Follow these steps to build and run **Steel Count** on your machine:

### ✅ Prerequisites

- Android Studio (latest stable version)
- Android SDK 21 or higher
- Internet connection (for mock backend or real AI integration)

---

### 🛠️ Setup Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/steel-count.git
   cd steel-count
````

2. **Open in Android Studio**

   * Launch Android Studio
   * Choose **File → Open** and select the `steel-count` folder

3. **Build the project**

   * Let Gradle sync complete
   * If prompted, install any missing SDK packages or plugins

4. **Run the app**

   * Connect an Android device or start an emulator
   * Click the **Run** ▶️ button in Android Studio

---

## 🧪 Testing

You can test the app using:

* Emulator with Camera enabled (for image input)
* Real device (recommended for photo features)

> The app currently uses a **mock backend** that simulates item detection. Real AI integration can be added later.

---

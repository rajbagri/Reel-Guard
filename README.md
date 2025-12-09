# Reel Guard

Reel Guard is an Android app built with **Jetpack Compose**, designed to track Instagram Reel views in real-time using an **Accessibility Service**, **ViewModel**, and a **Shared Repository pattern**.

It provides users with:

* Real-time reel view tracking
* Total watch count statistics
* Smooth UI built with Jetpack Compose
* Background tracking using Accessibility Service
* Persistent state using a shared repository & ViewModel

---

## 🚀 Features

* 📌 Track every reel you watch on Instagram
* 📊 Display live counters in Jetpack Compose UI
* 🔄 Uses ViewModel + Repository to maintain accurate state
* 🛡 Works entirely on-device
* 🔔 Sends notifications for milestones (coming soon)

---

---

## 🧩 Tech Stack

* **Kotlin**
* **Jetpack Compose**
* **ViewModel + StateFlow**
* **AccessibilityService**
* **Shared Repository Pattern**
* **Material 3**

---

## 🖼️ App Screenshots

Here are the app screenshots:

```markdown
![Screenshot 1](https://github.com/rajbagri/Reel-Guard/blob/main/WhatsApp%20Image%202025-11-10%20at%2014.05.04_97dde31a.jpg?raw=true)
![Screenshot 2](https://github.com/rajbagri/Reel-Guard/blob/main/WhatsApp%20Image%202025-11-10%20at%2014.07.58_aad827ef.jpg?raw=true)
![Screenshot 3](https://github.com/rajbagri/Reel-Guard/blob/main/WhatsApp%20Image%202025-11-10%20at%2014.11.02_b71b1da6.jpg?raw=true)
![Screenshot 4](https://github.com/rajbagri/Reel-Guard/blob/main/WhatsApp%20Image%202025-11-10%20at%2014.05.02_93998337.jpg?raw=true)

```

> Added `?raw=true` so GitHub displays the images correctly.

---

## 📸 Demo Video

You can embed your app demo video here:

```html
<video width="350" controls>
  <source src="YOUR_VIDEO_LINK_HERE.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>
```

> Replace `YOUR_VIDEO_LINK_HERE.mp4` with your GitHub-hosted video link or YouTube video download link.

---

## 📦 Installation

1. Clone the repository:

```bash
git clone https://github.com/yourusername/reel-guard.git
```

2. Open in Android Studio
3. Sync Gradle and run the project

---

## 🛠 How Reel Tracking Works

* AccessibilityService listens to **Instagram Reel view changes**
* Extracts the Reel ID or unique accessibility text
* Updates repository counter
* ViewModel observes repository using **StateFlow**
* UI automatically recomposes

---


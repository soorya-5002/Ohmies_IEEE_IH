# Ohmies_IEEE_IH
# 🧍‍♂️ Posture Monitoring System

A wearable posture monitoring device built using **ESP32**, **MPU6050**, and a **flex sensor** to detect and classify different levels of spinal posture. The system alerts the user in real time using a **buzzer** and **LED**, helping improve ergonomic awareness and reduce health issues related to poor posture.

---

## 🚀 Current Status (Hackathon Submission)
- ✅ MPU6050 integrated and calibrated for pitch angle detection  
- ✅ Flex sensor added for neck bend monitoring  
- ✅ Real-time posture classification implemented (Good, Moderate, Bad, Very Bad)  
- ✅ Buzzer and LED alert system integrated  
- 🛠️ Threshold tuning & mobile connectivity in progress

---

## 📸 Demo Setup
> *(You can add an image here of your setup or schematic later)*  
- Sensor placement: back or neck region  
- Real-time feedback via LED and buzzer based on posture classification

---

## 🔧 Hardware Used
- ESP32 Dev Board  
- MPU6050 (Accelerometer + Gyroscope)  
- Flex Sensor (resistance-based bend sensor)  
- 10kΩ Resistor (for voltage divider with flex)  
- Piezo Buzzer  
- LED  
- Jumper wires, breadboard

---

## 💡 How It Works
1. **MPU6050** detects the user's body tilt using the pitch angle.
2. **Flex sensor** detects neck bending via resistance change.
3. Based on calibrated thresholds, posture is categorized into four levels:
   - **Good**
   - **Moderately Bad**
   - **Bad**
   - **Very Bad**
4. If poor posture is detected:
   - A **buzzer beeps** at a specific frequency.
   - An **LED lights up** to visually notify the user.

---
## How to Download 
1. ** Clone the Repository**
open **Terminal** or **Git Bash**, navigate to your desired folder and run:
```plaintext
git clone https://github.com/soorya-5002/Ohmies_IEEE_IH.git
cd Ohmies_IEEE_IH
```
## 📟 Serial Output Example
```plaintext
AnglePitch: 28.56
Flex Sensor ADC: 712
BAD POSTURE
```

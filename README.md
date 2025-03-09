Here's the **README.md** file for the project details:  

---

## **QKart_QA - E-commerce Test Automation**  
**QKart_QA** is an automated testing project designed to ensure the quality and reliability of an **e-commerce website**. 
The project utilizes **Selenium WebDriver**, **XPath**, **TestNG**, and **Apache POI** to create a robust test automation framework, with **Gradle** as the build tool.  

---

## **🔹 Technologies & Tools Used**  
- **Automation Framework**: Selenium WebDriver  
- **Test Framework**: TestNG  
- **Locator Strategy**: XPath  
- **Synchronization**: Implicit & Explicit Waits  
- **Data-Driven Testing**: Apache POI (Excel-based testing)  
- **Build Tool**: Gradle  

---

## **🔍 What We Have Done in This Project?**  

### ✅ **1. Debugging & Logging**  
- Implemented **log statements** to identify failing test cases and debug errors efficiently.  

### ✅ **2. Automated UI Testing with Selenium**  
- Developed test scripts to validate key e-commerce functionalities such as:  
  - User Registration & Login  
  - Product Search & Filtering  
  - Add to Cart & Checkout  
  - Payment Processing  

### ✅ **3. Improved Synchronization with Waits**  
- Used **implicit and explicit waits** to handle **synchronization issues** and ensure stable test execution.  

### ✅ **4. Enhanced XPath Locators**  
- Optimized XPath strategies to improve the accuracy and performance of element selection.  

### ✅ **5. Migrated Tests to TestNG**  
- Structured test cases using **TestNG**, implementing:  
  - **Parameterized Tests**  
  - **Grouping & Prioritization**  
  - **Assertions for Validation**  

### ✅ **6. Data-Driven Testing with Apache POI**  
- Integrated **Apache POI** to read test data from **Excel files**, enabling robust data-driven testing.  

---

## **📂 Project Structure**  
```
QKart_QA/
│-- src/
│   ├── main/java/com/qkart/pages/  # Page Object Model (POM) classes
│   ├── test/java/com/qkart/tests/  # Automated test scripts
│-- testdata/                        # Excel files for data-driven testing
│-- build.gradle                      # Gradle build configuration
│-- testng.xml                        # TestNG test suite configuration
│-- README.md                         # Project documentation
```

---

## **🚀 How to Set Up & Run the Tests**  

### **1️⃣ Clone the Repository**  
```bash
git clone https://github.com/Shubham25-10/QKart_QA.git
cd QKart_QA
```

### **2️⃣ Build the Project using Gradle**  
Ensure **Java 8+**, **Gradle**, and **TestNG** are installed. Then, run:  
```bash
gradle build
```

### **3️⃣ Execute Test Cases**  
To run tests with **TestNG**, use:  
```bash
gradle test
```

---

## **📌 Eligibility**  
This project is suitable for individuals with knowledge of:  
- **Selenium WebDriver** for automation  
- **XPath** for locating elements  
- **TestNG** for test management  
- **Apache POI** for data-driven testing  
- **Java & Gradle** for development and dependency management  

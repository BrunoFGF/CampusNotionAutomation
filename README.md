# Automation Software for University Campus Tasks

## Overview

The primary goal of this software is to automate the process of logging into the virtual campus of my university, navigating to the dashboard, extracting task details, and saving them to a Notion database. This solution aims to save time and ensure that all university tasks are consistently tracked in Notion. Additionally, the project is configured to run as a background task in Windows, allowing it to execute automatically at scheduled times.

## Table of Contents

- [Motivation](#motivation) 
- [Features](#features) 
- [Built With](#builtWith) 
- [Installation](#installation)
- [Contributing](#contributing)
- [License](#license) 

## Motivation

I created this project because I regularly use Notion to keep track of my university tasks. Initially, I entered the tasks manually, but over time it became tedious and easy to forget. Automating this process ensures that my tasks are always up-to-date in Notion without manual effort.

## Features

- Automated Login: Logs into the university's virtual campus using your credentials.
- Task Extraction: Navigates through each task in the dashboard, extracts the URL, subject name, task name, and delivery deadline.
- Notion Integration: Formats the extracted information for compatibility with Notion and saves it to a Notion database.
- Data Comparison: Compares new tasks with those already saved in Notion to avoid duplication.
- Secure Credentials: Credentials, campus links, and Notion API keys are stored locally in environment variables and accessed via the application.properties file.
- Background Task: Configured to run as a scheduled background task in Windows, allowing the automation to execute at predefined times.

## Built With

- **Programming Languages**:
  - Java

- **Build Tools**:
  - Maven

- **Dependencies**:
  - **jsoup** (version 1.17.2): For parsing HTML documents.
  - **Apache HttpClient** (version 4.5.14): For making HTTP requests.
  - **Jackson Databind** (version 2.17.1): For processing JSON.
  - **JUnit Jupiter API** (version 5.10.2): For unit testing.
  - **Selenium Java** (version 4.21.0): For browser automation.
  - **dotenv-java** (version 2.2.0): For managing environment variables.

- **Task Management Tools**:
  - **Notion**: Used for task automation, and save them in Notion.

- **Web Driver**:
  - **ChromeDriver**: Required for Selenium to control the Chrome browser.

- **Development Environment**:
  - **Oracle JDK**: Required for compiling and running the Java application.
  - **Maven**: Used for running Maven commands to build the .jar file.

## Installation

### Prerequisites

1. Download Java Development Kit (JDK) and Maven

  - Download and install JDK in a path that you can remember.
  - Download and install Maven in a path that you can remember.
  - Add both bin file to the PATH environment variables.

2. Download an IDE for Java and install it
  - IntelliJ 😉   

3. Download ChromeDriver

  - Download the ChromeDriver that matches your Chrome browser version.
  - Extract the downloaded file and save it in a path that you can remember.

4. Download and Install Git Bash

  - Go to the Git for Windows download page.
  - Download the installer and run it.
  - Follow the installation prompts, using the default settings.
  - After installation, open Git Bash by searching for it in the Start menu.

5. Download Notion and create a Notion Page and Database

  - Download Notion and install it.
  - Create a new page in Notion.
  - Add a new table with the following columns:
    - Name (Title)
    - Subject (Text)
    - Due Date (Date)
    - Link (URL)
    - Completed (Checkbox, optional)

6. Get Notion Database ID and API Token

  - Go to Notion, click on "Settings & Members", then "Integrations".
  - Click on "Develop or manage integrations".
  -  Create a new integration, name it "Universidad de Guayaquil"(name is up to you) , and choose the type.
  - After creating it, add this integration to your database:
    - Open your Notion database.
    - Click on "Share" in the top-right corner.
    - Select "Invite" and search for your integration name to add it.

7. Create Environment Variables

  - Set the following environment variables on your system:
  - **Variable Name**: Value
    - **campus.dashboard.url**: Your campus dashboard URL
    - **campus.login.url**: Your campus login URL
    - **campus.password**: Your campus password
    - **campus.username**: Your campus username
    - **notion.database.id**: Your Notion database ID
    - **notion.token**: Your Notion API token
    - **notion.version**: Notion version (e.g., "2022-06-08")

### Steps for clone and installing

1. Clone the Repository:

  - Open Git Bash by searching for it in the Start menu.
  - Create a new folder for your project and navigate to it:
    - **Creating a folder at the location you are**: mkdir "folder name" (e.g., "mkdir AutomationProject")
    - **Navigating to that folder**: cd "folder name" (e.g., "cd AutomationProject")
  - Clone the repository:
    - git clone https://github.com/BrunoFGF/CampusNotionAutomation.git

3. Open Project in IntelliJ IDEA

  - Open IntelliJ IDEA.
  - Select "Open or Import" and navigate to the cloned repository folder.
  - IntelliJ IDEA will automatically detect and import the Maven project.
  - Once the project is open, you need to load Maven dependencies:
    - Right-click on the project root in the Project view.
    - Select "Reload Maven Project".

4. Configure ChromeDriver Path

  - Open the file src/main/java/CampusScraper.java.
  - Navigate to line 27 and locate the following line:
    - System.setProperty("webdriver.chrome", "C:\\Users\\Bruno\\Desktop\\Chrome Driver\\chrome.exe");
  - Replace the path with the location of your chrome.exe file:
    - System.setProperty("webdriver.chrome", "path_to_your_chrome_driver\\chrome.exe");
  - Save the file.

To this point you can run it and use it, but it's not efficient right.

### Steps for working in Windows as a background task

1. Initial Setup

  - Run the Program Once
    - Ensure the program runs successfully at least once to verify there are no issues and create .class files.

2. Generate the JAR File

  - Identify Project Location
  - Determine the location of your project. It should look something like this:  
    C:\Users\User\Your\Path\CampusNotionAutomation\NotionAutomation
  - Open Command Prompt
    - Open the Command Prompt by pressing Win + R, typing cmd, and pressing Enter.
  - Navigate to Project Location
    - Use the cd command to navigate to the project directory: cd C:\Users\User\Your\Path\CampusNotionAutomation\NotionAutomation
  - Verify the pom.xml File
    - Ensure the pom.xml file is present by using the dir command: dir.
  - Generate the JAR File
    - Run the following Maven command to clean and package the project: mvn clean package
    - The JAR file will be created in the target folder.

3. Create a Batch File

  - Open Git Bash
    - Open Git Bash by searching for it in the Start menu.
  - Navigate to Project Location
    - Use the cd command to navigate to the project directory: cd C:\Users\User\Your\Path\CampusNotionAutomation
  - Create a Batch File
    - Create a new file named runApplication.bat: touch runApplication.bat
  - Edit the Batch File
    - Right-click on runApplication.bat and select "Edit".
    - Paste the following code, replacing the path with your target folder path:
      @echo off
      cd /d C:\Users\User\Your\Path\CampusNotionAutomation\NotionAutomation\target
      start /B javaw -jar Application.jar
      exit
    - Save and close the file.

4. Schedule the Task

  - Open Task Scheduler
    - Press Win + R, type taskschd.msc, and press Enter.
  - Create a New Task
    - In Task Scheduler, click "Create Task".
  - General Settings
    - Provide a name and description for the task (e.g., "University Campus Automation").
  - Trigger Settings
    - Go to the "Triggers" tab.
    - Click "New" and set the schedule for when you want the task to run (e.g., daily at a specific time).
  - Action Settings
    - Go to the "Actions" tab.
    - Click "New" and select "Start a program".
    - In the "Program/script" field, browse and select the runApplication.bat file located in: 
      C:\Users\User\Your\Path\CampusNotionAutomation
  - Finalize and Save
    - Click "OK" to apply and save the task.
    - Your task is now scheduled and will run the batch file at the specified times.

## Contributing
Contributions are welcome! Here's how you can contribute to this project:

1. Fork the repository.
2. Create a new branch: git checkout -b feature-name.
3. Make your changes.
4. Test your changes thoroughly.
5. Push your branch: git push origin feature-name.
6. Create a pull request.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
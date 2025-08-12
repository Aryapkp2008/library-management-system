@echo off
echo Building Library Management System...

REM Create build directory
if not exist "build" mkdir build
if not exist "dist" mkdir dist

REM Compile all Java files
echo Compiling Java files...
javac -d build src/*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM Create manifest file for GUI version
echo Creating manifest for GUI version...
echo Main-Class: LibraryManagementSystemGUI > build\MANIFEST-GUI.MF
echo. >> build\MANIFEST-GUI.MF

REM Create manifest file for Console version
echo Creating manifest for Console version...
echo Main-Class: LibraryManagementSystem > build\MANIFEST-CONSOLE.MF
echo. >> build\MANIFEST-CONSOLE.MF

REM Create JAR files
echo Creating JAR files...
cd build
jar cfm ..\dist\LibraryManagementSystem-GUI.jar MANIFEST-GUI.MF *.class
jar cfm ..\dist\LibraryManagementSystem-Console.jar MANIFEST-CONSOLE.MF *.class
cd ..

echo.
echo Build completed successfully!
echo.
echo Generated files:
echo - dist\LibraryManagementSystem-GUI.jar (GUI version)
echo - dist\LibraryManagementSystem-Console.jar (Console version)
echo.
echo To run:
echo - GUI version: java -jar dist\LibraryManagementSystem-GUI.jar
echo - Console version: java -jar dist\LibraryManagementSystem-Console.jar
echo.
pause

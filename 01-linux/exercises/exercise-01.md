# Exercise: Log File Analyzer Script

## Objective
Create a shell script that analyzes a log file and generates a summary report.

## Requirements

Create a script called `log-analyzer.sh` that:

1. Takes a log file path as a command-line argument
2. Creates a sample log file if the specified file doesn't exist (for testing)
3. Counts the total number of lines in the log file
4. Counts how many lines contain "ERROR", "WARNING", and "INFO"
5. Extracts and displays unique IP addresses (if any) from the log
6. Outputs a formatted summary report

## Sample Log Format
```
2024-01-15 10:23:45 INFO User login successful from 192.168.1.100
2024-01-15 10:24:12 ERROR Database connection failed
2024-01-15 10:25:33 WARNING High memory usage detected
2024-01-15 10:26:01 INFO Request processed from 10.0.0.50
```

## Expected Output
```
========== LOG ANALYSIS REPORT ==========
File: /path/to/logfile.log
Total Lines: 100
-----------------------------------------
INFO:    45
WARNING: 30
ERROR:   25
-----------------------------------------
Unique IP Addresses Found:
  - 192.168.1.100
  - 10.0.0.50
=========================================
```

## Skills Tested
- Shell script basics (shebang, variables)
- Command-line arguments (`$1`, `$#`)
- Conditional statements (`if`, `else`)
- Text processing (`grep`, `wc`, `sort`, `uniq`)
- Regular expressions basics
- File operations

## Bonus Challenge
- Add a flag `-v` for verbose output that shows each log entry categorized
- Calculate and display the percentage of each log level

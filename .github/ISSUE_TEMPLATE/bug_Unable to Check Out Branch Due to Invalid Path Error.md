---
title: Unable to check out branch due to invalid path error for file with special characters.

assignees: larissaspascascio

---

**Description**
When attempting to check out the branch origin/updated-code, the operation fails with the following error message:
could not check out origin/updated-test, invalid path 'src/output/Report_Cities_filtered_by_continent:_North_America.txt'.

It seems the issue is related to the filename, which includes special characters such as a colon (:). This likely causes conflicts on file systems that do not allow such characters in filenames (e.g., Windows).

**To Reproduce**
Steps to reproduce the behavior:
1. Clone the repository conatining the branch updated -code
2. Run the command 'git checkout origin/updated-test'
3. Observe the error message related to the invalid path.
4. See error

**Expected behavior**
The branch should be checked out without any errors, and all files should be available locally.

**Screenshots**
![WhatsApp Image 2024-12-02 at 09 50 37_ab4edd9b](https://github.com/user-attachments/assets/bd7a2cf1-6704-40a8-82aa-58cd1c2b9b72)

**Enviornment****
 - OS: Windows 11
 - Git Version 24.3.2
 - File System NTFS (Windows)

**Impact**
This issue prevents contributors working on Windows systems from checking out the branch or collaborating on the project effectively.

**Suggested Fix**
1. Replace colons (:) in filenames with an alternative character (e.g., underscore _ or hyphen -).
2. Standardize naming conventions for file paths to ensure compatibility across different operating systems.

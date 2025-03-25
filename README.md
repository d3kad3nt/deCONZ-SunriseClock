# deCONZ-SunriseClock

This is an App that triggers deCONZ/Hue actions for the next alarm clock. You can connect to your bridge and select the
lights that should be activated at the alarm time. **This app is not released yet.**

## Development

Android Studio is used to develop this app. Most of the settings for code styling and formatting are shared, but some
can't be added to this git repository. Suggested and recommended settings are described below. These settings have to be
set manually. The settings screen in Android Studio can be accessed through File -> Settings.

### Android Studio settings

Settings -> Version Control -> Commit

- Use none-modal commit interface: Checked
- Force non-empty commit comments: Checked
- Blank line between subject and body: Checked
    - Severity: Warning
- Limit body line: Checked
    - Severity: Warning
    - Right margin: 72
    - Show right margin: Checked
    - Wrap when typing reaches right margin: Checked
- Limit subject line: Checked
    - Severity: Warning
    - Right margin: 72
- Spelling: Checked
    - Severity: Typo
- Optimize imports: Checked
- Analyze code: Checked
    - Profile: Stored in Project -> SunriseClock

Settings -> Editor -> Design Tools -> Default Editor Mode

- Drawables: Split
- Other Resources (e.g. Layout, Menu, Navigation): Design

Settings -> Build, Execution, Deployment -> Deployment

- Automatically perform "Run" when Apply Changes fails: Checked
- Automatically perform "Run" when Apply Code Changes fails: Checked

Settings -> Tools -> Actions on Save

- Optimize imports: Checked
    - All file types

Settings -> Tools -> Terminal

- Audible bell: Unchecked

### Recommended Plugins

This project uses [https://github.com/diffplug/spotless](Spotless) to format the code. To use that formatter also in
Android Studio you have to install the spotless plugin https://plugins.jetbrains.com/plugin/18321-spotless-gradle

### Android Studio tips and tricks

Tasks -> Servers:
Android Studio can create a new branch for an issue created on GitHub.

- Host: https://github.com
- Repository: d3kad3nt/deCONZ-SunriseClock
- Api token : [GENERATE]
  App
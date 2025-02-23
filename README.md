

# **Mortal Minion** - Android App

**Mortal Minion** is an Android app designed to give users a perspective of how much of their life has been lived and how much remains based on their Date of Birth (DOB). The app considers the average life expectancy of humans (set by default to 75 years, but editable by the user) and shows the user's progress in terms of:

- Years
- Days
- Hours

### Features:

- **User-friendly Date of Birth (DOB) input**: Users can select their DOB using an intuitive DatePicker.
- **Dynamic Age Calculation**: The app calculates the user's age in years, days, and hours based on the current date.
- **Life Expectancy Slider**: The app displays how much of the user's life has been lived and how much is remaining, with the ability to adjust life expectancy.
- **Editable Life Expectancy**: By default, the life expectancy is set to 75 years, but users can adjust this value according to their preferences.
- **Real-time Update**: As the user adjusts their DOB or life expectancy, the app dynamically updates the displayed age and remaining life perspective.

### Requirements:

- **Android Studio**: To build and run the app.
- **Kotlin**: The app is developed using Kotlin.
- **Android API Level 21+**: The minimum required Android API level for the app.

### Getting Started:

1. **Clone/Download the Project**:
   You can clone or download the repository to your local machine.

   ```bash
   git clone https://github.com/yourusername/mortal-minion.git
   ```

2. **Open the Project**:
   Open the project in Android Studio.

3. **Run the App**:
   - Connect an Android device or use an Android Emulator.
   - Click on the **Run** button in Android Studio.

4. **Permissions**:
   - The app does not require special permissions (for location or storage) but may require an internet connection for any future enhancements such as location-based life expectancy data.

### How to Use:

1. **Set Your Date of Birth**:
   - The app will prompt you to select your Date of Birth using a `DatePicker`. After selecting your birth date, the app will calculate your age in years, days, and hours.

2. **Edit Life Expectancy**:
   - By default, the life expectancy is set to 75 years. However, you can adjust this value using the **Life Expectancy Slider**. The slider ranges from 50 to 100 years, and the app will update the remaining years, days, and hours accordingly.

3. **View Your Life Perspective**:
   - The app will display how much of your life has been lived and how much is remaining based on the inputted DOB and life expectancy.

### App UI:

- **DatePicker**: Allows users to input their Date of Birth.
- **TextView**: Displays the calculated age in years, days, and hours.
- **SeekBar (Slider)**: Displays a life expectancy scale (default 75 years), which can be adjusted by the user.
- **Real-Time Updates**: As you interact with the DatePicker or SeekBar, the app dynamically updates the displayed age and remaining life data.

### Code Explanation:

1. **DatePicker**: 
   - This component allows users to select their DOB, and the app calculates their current age based on the date selected.
   
2. **Age Calculation**:
   - The app calculates the difference between the current date and the DOB in years, days, and hours. It uses the `Calendar` class to calculate the exact age in real-time.

3. **Life Expectancy Slider**:
   - A slider is used to display how much of the user's life has passed in relation to their selected life expectancy. The slider ranges from 50 to 100 years, and users can adjust it.

4. **Dynamic Updates**:
   - When users select a new DOB or adjust the life expectancy slider, the app dynamically updates the displayed age and the life expectancy values.

### Example Screenshots:

*(Include some sample screenshots of the app here, if available.)*

### Future Enhancements:

- **Location-Based Life Expectancy**: The app can fetch life expectancy data based on the user's geographical location (e.g., country) using an API.
- **Milestones and Notifications**: Implement milestone notifications like "You’ve lived for 10,000 days!" or "You’ve crossed 50% of your expected lifespan!".
- **Customizable Themes**: Add options to change the app's theme and personalize the design.

### Credits:

- The app uses Android SDK components such as `DatePicker`, `SeekBar`, and `TextView` to create the user interface and perform age calculations.


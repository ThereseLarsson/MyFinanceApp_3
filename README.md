# da401a_assignment_1
## About
Assignment 1 from a course at bachelor degree education. The implemented application provides a financial app where the user can e.g.:
- add income / outcome consisting of title, date, amount and category from a drop down menu.
- see a summary over the users total income / outcome amount and total balance.
- see all income / outcome items in a list
  - click an individual item to display more detailed information in a new window. The information includes: title, date added, amount, category and an icon that corresponds to the items category.
  
## From education
Education: Systemutvecklare (bachelor degree) (eng. Software developer) at Malmö University

Course: Application Development for Android (DA401A).

Went the course: ht 2018.

## Run the application
- Run MainActivity.java

### Left TODO (last updated 23/11/2019, kl. 19:43)

- Make it possible to view a specific item in a detailed view (1) 
  - Almost done except for one BUG: DetailActivity only shows income items regardless of if the listview in the ViewTransactionFragment is displaying income- or outcome items
  - Relevant classes
    - ViewTransactionFragment
    - DetailActivity
    
- Fix so the app can handle screen rotation (2)
  - Relevant classes:
    - CreateAccount
    - DetailActivity
    - EnterTransactionActivity
    - ~~GreetingFragment~~
    - ~~SummaryFragment~~
    - ViewTransactionFragment
    - Startup(Activity)
    - ... Maybe more?


# AI Usage Tracking

Tag | AI Agent | Description of Usage | Prompt | Reflection
---|:---:|:---:|:---:
![]Level-3 | Github Copilot | "Add the ability to mark tasks as done. Optionally, add the ability to change the status back to not done.

These are the changes to be implemeted

a new command will be accomodated. this will be in the form of "mark i" where i is the index of the task to be marked as completed. alongisde this will be a command "unmark i" where i is the index of the task to be unmarked if currently marked, and no change if currently already unmarked. the index and the mark status of a task should be stored in a an additional array

the return format for the "list" instruction should include an 'X' to indicate whether a task has been flagged as marked or not,

the format of the input and output for list, mark, and unmark is shown below

list
____________________________________________________________
Here are the tasks in your list:
1.[X] read book
2.[ ] return book
3.[ ] buy bread
____________________________________________________________

mark 2
____________________________________________________________
Nice! I've marked this task as done:
[X] return book
____________________________________________________________

unmark 2
____________________________________________________________
OK, I've marked this task as not done yet:
[ ] return book
____________________________________________________________" | It was incredibly easy to tell the agent exactly what to do once i understood the requirements of this level. It was surprising that copilot managed to do the task exactly as described in the first try itself. It saved quite some time as I am not so fast at typing but since I already knew exactly how i wanted it implemented it was easy to write down exactly what i wanted, what data structure and output format i needed, and just wait a few seconds for it to do its work
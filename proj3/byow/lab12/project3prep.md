# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer:
Recursive! Never thought about that.
-----
**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer:
For Project 3, the hexagon might be every "block", in which there will either be a room or a hallway. All blocks are tessellated, 
so the rooms and hallways should also be connected in some ways.
-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer:
Generate block, then generate either room or hallway randomly.
-----
**What distinguishes a hallway from a room? How are they similar?**

Answer:
A hallway is just a room of width = 1 or height = 1, the other side not being 1.

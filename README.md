# Implementation of Operating System Functions

###1. HW1 - Linkers
> Originally called a 'Linkage Editor' by IBM.

- A **linker** is an example of a utility program an os. 
- Like a compiler, the linker is not part of the operating system per se. Linker does not run in supervisor mode. 
- Unlike a compiler, it is OS dependent (what object/load file format is used) and is not (normally) language dependent.

- When the compiler and assembler have finished processing a module, they produce an object module that is almost runnable. There are two remaining tasks to be accomplished before object modules can be run. Both are involved with linking together multiple object modules. The tasks are *relocating relative addresses* and *resolving external references*.
- The output of a linker is called a load module because, with relative addresses relocated and the external addresses resolved, the module is ready to be loaded and run.


# Gitlet Design Document

**Name**: Ruotian Zhang

## Classes and Data Structures

### Commit

#### Instance Variables
* message - contains the message of a commit
* timestamp - time at which a commit was created. Assigned by the constructor
* parent - the parent commit of a commit object
* id - the id of this commit object
* committedMap - a map contains all tracked files of this commit

1. Commit(): Constructor for init commit
2. Commit(String messgae, HashMap map, String parent): Constructor for fillowing commits
3. save(): Save the id of a commit into a file also named this id.
4. getters


### Blobs

#### Instance Variables
* content - the byte[] content of the file which this blob object are to store
* id - the id of this blob object

1. Blob(File file): Create a blob object with a file object
2. save(): Save the blob object into a file named its id.
3. getters

### Main

#### Instance Variables
* None

1. checkArgsNums(String[] args, int targetNum): Check if the num of given arguments is valid
2. main(String[] args)

### Repository

#### Instance Variables
* CWD
* GITLET_DIR
* OBJECTS_DIR
* COMMITS_DIR
* BLOBS_DIR
* STAGE
* BRANCH_DIR
* MASTER
* HEAD
* ignoreFiles

1. getHEADBranchName()
2. getHEADCommit()
3. init()
4. add(String fileName)
5. commit(String message)
6. log()
7. glog()
8. printCommitFrom(String commitId)
9. ....

### Stage

#### Instance Variables
* stagingMap
* removingMap

1. Stage()
2. readStage()
3. saveStageFile()
4. add(String fileName)
5. add(String fileName, String blobId)
6. add(String fileName, Blob blob)
7. rmStaging(String fileName)
8. clearMap()
9. staged(String fileName)
10. getters


## Algorithms

## Persistence


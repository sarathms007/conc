import java.io.*;
class queue
{
final int msize;
int queue[];
int front, nItems, rear;
queue(int siz)
{
this.msize = siz;
this.queue = new int[msize];
this.front = this.nItems = 0;
this.rear = -1;
}
public void insert(int j) {
       if(rear == msize-1)
           rear = -1;
       queue[++rear] = j;
       nItems++;
       }
    public int remove() {
       int temp = queue[front++];
       if(front == msize)
          front = 0;
       nItems--;
       return temp;
       }
    public boolean isEmpty() {
       return(nItems==0);
       }
    public boolean isFull() {
       return(nItems==msize);
       }
}

class pc extends Thread
{
queue q;
int id;
static int i =0;
String type;
pc(String type, int id, queue q)
{
this.type = type;
this.id = id;
this.q = q;
}
public void produce(int i)
{
System.out.println("Producer"+id+" is producing "+i);
q.insert(i);
}
public void producer()
{
synchronized(q) {
while(!q.isFull()) {
produce(i++);
if(i%q.msize==0)
{i=0;break;}
if(i%3==0)
break;
}
if(!q.isFull())
q.notifyAll();
}
}	
public void consume()
{
System.out.println("\tconsumer"+id+" is consuming "+q.remove());
}
public void consumer()
{
synchronized(q) {
while(!q.isEmpty()) {
consume();
break;
}
if(q.isEmpty())
q.notifyAll();
}
}	
public void run()
{
while(true) {
if(this.type.equals("prod"))
{producer();
try{Thread.sleep(1000);} catch (Exception e){}}
else
{consumer();
try{Thread.sleep(1000);} catch (Exception e){}}
}
}
}

class prod3cons{
public static void main(String[] args) {
queue qu = new queue(6);
int i=0;
pc pc1[] = new pc[4];
pc1[i] = new pc("prod", i, qu);
for(i=1;i<4;i++)
pc1[i] = new pc("cons",i ,qu);
for(i=0;i<4;i++)
pc1[i].start();
}
}

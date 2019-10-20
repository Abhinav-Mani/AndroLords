package com.androlord.prayaas.DataClass;

public class GroupMessage implements Comparable<GroupMessage>{
    public String messege;
    public String user;
    public long time;

    public GroupMessage(String messege,String user,long time)
    {
        this.time=time;
        this.user=user;
        this.messege=messege;
    }

    @Override
    public int compareTo(GroupMessage o) {
        if(this.time<o.time)
            return 1;
        else if(this.time>o.time)
            return -1;
        return 0;
    }
}

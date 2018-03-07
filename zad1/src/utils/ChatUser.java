package utils;

import java.io.PrintWriter;
import java.net.InetAddress;

public class ChatUser {
    private final String nickName;
    private final PrintWriter tcpWriter;
    private final InetAddress inetAddress;

    public ChatUser(String nickName, PrintWriter tcpWriter, InetAddress inetAddress) {
        this.nickName = nickName;
        this.tcpWriter = tcpWriter;
        this.inetAddress = inetAddress;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatUser chatUser = (ChatUser) o;

        return nickName != null ? nickName.equals(chatUser.nickName) : chatUser.nickName == null;
    }

    @Override
    public int hashCode() {
        return nickName != null ? nickName.hashCode() : 0;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public String getNickName() {
        return nickName;
    }

    public PrintWriter getTcpWriter() {
        return tcpWriter;
    }
}

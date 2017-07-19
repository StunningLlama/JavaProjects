import java.io.*;
import java.net.*;
import java.util.List;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import com.google.gson.*;


public class MineChat {
	

	public static int readVarInt(DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }
	
	public static String arrToStr(byte[] b)
	{
		String returnv = "";
		for(byte i: b)
		{
			returnv += (String.valueOf(i&0xFF) + ", ");
		}
		return ((returnv.length() == 0)? "NULL":returnv.substring(0, returnv.length() - 2));
	}
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		String ip = "50.141.200.40";
       
		Socket socket = new Socket(InetAddress.getByName(ip), 25565);
		DataOutputStream server = new DataOutputStream(socket.getOutputStream());
		OutputPacket handshake = new OutputPacket(0x00);
		handshake.writeVarInt(5);
		handshake.writeString(ip);
		handshake.writeShort(25565);
		handshake.writeByte(2);
		handshake.sendPacket(server);
		
		//OutputPacket ping = new OutputPacket(0x00);
		//ping.sendPacket(server);
	
		DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        //InputPacket jsonp = new InputPacket(dataInputStream);
        
        
        
        //String j = jsonp.readString();
        
        OutputPacket LoginStart = new OutputPacket(0x00);
        LoginStart.writeString("thepowderguy");
        LoginStart.sendPacket(server);
        
        
        System.out.println(MineChat.readVarInt(dataInputStream));
        if (MineChat.readVarInt(dataInputStream) == 0x01)
        {
        	System.out.println("Start encryption");
        	short var1 = dataInputStream.readShort();

            if (var1 < 0)
            {
                throw new IOException("Key was smaller than nothing!  Weird key!");
            }
            else
            {
                byte[] var2 = new byte[var1];
                dataInputStream.readFully(var2);
              	 System.out.println(arrToStr(var2));
            }
        	//dataInputStream.readFully(bx);
        	
        	 /*System.out.println( MineChat.readVarInt(dataInputStream));
        	 int x = MineChat.readVarInt(dataInputStream);
        	 System.out.println(x);
        	 byte[] bx = new byte[x];
        	 dataInputStream.readFully(bx);
        	 int y = MineChat.readVarInt(dataInputStream);
        	 System.out.println(y);
        	 byte[] by = new byte[y];
        	 dataInputStream.readFully(by);
        	 System.out.println(arrToStr(bx));
        	 System.out.println(arrToStr(by));*/
        }
       // InputPacket Success = new InputPacket(dataInputStream);
       // if (Success.getId() != 0x02)
        //{
        //	if (Success.getId() == 0x01)
        //	{
        		
        //	}
    		/*System.out.println(Success.readString());
    		System.out.println(",");
    		System.out.println(arrToStr(Success.readByteArray()));
    		System.out.println(",");
    		System.out.println(arrToStr(Success.readByteArray()));
        	System.out.println(Success.readString());
        	System.out.println(arrToStr(Success.readByteArray()));
        	System.out.println(arrToStr(Success.readByteArray()));*/
        	//throw new RuntimeException("Invalid: " + Success.getId() + ", " + Success.readString());
       // }
        
        
        //System.out.println(Success.readString());
      //  System.out.println(Success.readString());
        
        InputPacket Join = new InputPacket(dataInputStream);
        
        Join.readInt();
        Join.readByte();
        Join.readByte();
        Join.readByte();
        Join.readByte();
        Join.readString();
        Join.readBoolean();
        
        InputPacket in;
        while (true)
        {
        	in = new InputPacket(dataInputStream);
        	System.out.println("Packet_ID=" + in.getId());
        	if (in.getId() == 0x00)
        	{
        		OutputPacket KeepAlive = new OutputPacket(0x00);
        		KeepAlive.writeVarInt(in.readVarInt());
        		KeepAlive.sendPacket(server);
        	}
        	else if (in.getId() == 0x02)
        	{
        		System.out.println(in.readString());
        		in.readByte();
        	}
        	else
        	{
        		in.discardData();
        	}
        }
        
        /*Gson gson = new Gson();
        StatusResponse r = gson.fromJson(j, StatusResponse.class);
        System.out.println("Version: " + r.getVersion().getName());
        System.out.println("Player Count: " + r.getPlayers().getOnline() + "/" + r.getPlayers().getMax());
        System.out.println("Description: " + r.getDescription());
        String list = "";
        try {for (Player p: r.getPlayers().getSample()) list += p.getName() + ", ";} catch(Exception e) {}
        System.out.println("Players: " + ((list.length() == 0)? "" : list.substring(0, list.length() - 2)));*/
        
        //socket.close();
	}
}


class OutputPacket
{
	ByteArrayOutputStream b;
    DataOutputStream data;
	
	public OutputPacket(int id)
	{
		b = new ByteArrayOutputStream();
		data = new DataOutputStream(b);
		try {
			this.writeVarInt(id);
		} catch (IOException e) {}
	}
	
	public void writeInt(int value)
	{
		try {
			data.writeInt(value);
		} catch (IOException e) {}
	}
	
	public void writeString(String value)
	{
		try {
			data.writeByte(value.length());
			data.writeBytes(value);
		} catch (IOException e) {}
	}
	
	public void writeByteArray(byte[] value)
	{
		try {
			data.writeByte(value.length);
			data.write(value);
		} catch (IOException e) {}
	}
	
	public void writeShort(int value)
	{
		try {
			data.writeShort(value);
		} catch (IOException e) {}
	}
	
	public void writeByte(int value)
	{
		try {
			data.writeByte(value);
		} catch (IOException e) {}
	}
	
	public void writeLong(long value)
	{
		try {
			data.writeLong(value);
		} catch (IOException e) {}
	}
	
	public void sendPacket(DataOutputStream server)
	{
		try {
			this.writeVarInt(server, this.b.size());
		server.write(b.toByteArray());
		} catch (IOException e) {}
	}
	
    public void writeVarInt(int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
              this.data.writeByte(paramInt);
              return;
            }
 
            this.data.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }
    
    public void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
            	out.writeByte(paramInt);
              return;
            }
 
            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }
}

class InputPacket
{
	ByteArrayOutputStream b;
    DataInputStream data;
    int size;
    int id;
    int bytesleft;
	
	public InputPacket(DataInputStream input)
	{
		b = new ByteArrayOutputStream();
		data = input;
		try {
			while (input.available() == 0)
			{
				Thread.sleep(10);
			}
		} catch (Exception e1) {}
		try {
			size = this.readVarInt();
			bytesleft = size;
			id = this.readVarInt();
		} catch (IOException e) {e.printStackTrace();}
		if (size < 1)
		{
			throw new RuntimeException();
		}
	}
	
	public int getSize()
	{
		return size;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int readInt()
	{
		bytesleft -= 4;
		try {
			return this.data.readInt();
		} catch (IOException e) {}
		return -1;
	}
	
	public String readString() throws IOException
	{
		int len = this.readVarInt();
		if (len == 0) return "";
		byte[] d = new byte[len];
		this.data.readFully(d);
		bytesleft -= len;
		return new String(d);
	}

	public byte[] readByteArray() throws IOException
	{
		int len = this.readVarInt();
		if (len == 0)
		{
			System.out.println("<0>");
		}
		byte[] d = new byte[len];
		this.data.readFully(d);
		bytesleft -= len;
		return d;
	}

	public int readShort()
	{
		bytesleft -= 2;
		try {
			return this.data.readShort();
		} catch (IOException e) {}
		return -1;
	}

	public int readByte()
	{
		bytesleft -= 1;
		try {
			return this.data.readByte();
		} catch (IOException e) {}
		return -1;
	}

	public long readLong()
	{
		bytesleft -= 8;
		try {
			return this.data.readLong();
		} catch (IOException e) {}
		return -1;
	}

	public boolean readBoolean()
	{
		bytesleft -= 1;
		try {
			return this.data.readBoolean();
		} catch (IOException e) {}
		return false;
	}
	
	public byte[] discardData()
	{
		byte[] d = new byte[size - 1];
		try {
			this.data.readFully(d);
		} catch (IOException e) {}
		return d;
	}
	
	public int readVarInt() throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = data.readByte();
            size--;
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }
	
	public int readVarInt(DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }
	
	
}




































class StatusResponse {
    private String description;
    private Players players;
    private Version version;
    private String favicon;
    private int time;

    public String getDescription() {
        return description;
    }

    public Players getPlayers() {
        return players;
    }

    public Version getVersion() {
        return version;
    }

    public String getFavicon() {
        return favicon;
    }

    public int getTime() {
        return time;
    }      

    public void setTime(int time) {
        this.time = time;
    }
    
}

class Players {
    private int max;
    private int online;
    private List<Player> sample;

    public int getMax() {
        return max;
    }

    public int getOnline() {
        return online;
    }

    public List<Player> getSample() {
        return sample;
    }        
}

class Player {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    
}

class Version {
    private String name;
    private String protocol;

    public String getName() {
        return name;
    }

    public String getProtocol() {
        return protocol;
    }
}


class CryptUtil {

	public static SecretKey generateSharedKey() {
		try {
			KeyGenerator gen = KeyGenerator.getInstance("AES");
			gen.init(128);
			return gen.generateKey();
		} catch(NoSuchAlgorithmException e) {
			throw new Error("Failed to generate shared key.", e);
		}
	}

	public static byte[] encryptData(Key key, byte[] data) {
		return runEncryption(1, key, data);
	}

	private static byte[] runEncryption(int mode, Key key, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance(key.getAlgorithm());
			cipher.init(mode, key);
			return cipher.doFinal(data);
		} catch(GeneralSecurityException e) {
			throw new Error("Failed to run encryption.", e);
		}
	}

	public static byte[] getServerIdHash(String serverId, PublicKey publicKey, SecretKey secretKey) {
		try {
			return encrypt("SHA-1", new byte[][] { serverId.getBytes("ISO_8859_1"), secretKey.getEncoded(), publicKey.getEncoded() });
		} catch(UnsupportedEncodingException e) {
			throw new Error("Failed to generate server id hash.", e);
		}
	}

	private static byte[] encrypt(String encryption, byte[]... data) {
		try {
			MessageDigest digest = MessageDigest.getInstance(encryption);
			for(byte array[] : data) {
				digest.update(array);
			}

			return digest.digest();
		} catch(NoSuchAlgorithmException e) {
			throw new Error("Failed to encrypt data.", e);
		}
	}

}
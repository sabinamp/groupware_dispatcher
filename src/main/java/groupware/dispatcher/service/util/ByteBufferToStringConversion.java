package groupware.dispatcher.service.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ByteBufferToStringConversion {

    public static ByteBuffer string2ByteBuffer(String s, Charset charset) {
        return ByteBuffer.wrap(s.getBytes(charset));
    }

    public static String byteBuffer2String(ByteBuffer myByteBuffer, Charset charset) {

        if (myByteBuffer.hasArray()) {
            return new String(myByteBuffer.array(),
                    myByteBuffer.arrayOffset() + myByteBuffer.position(),
                    myByteBuffer.remaining());
        } else {
            final byte[] b = new byte[myByteBuffer.remaining()];
            myByteBuffer.duplicate().get(b);
            return new String(b);
        }
    }
}

package org.eric;

import smallworld.core.Runner;
import java.io.ByteArrayInputStream;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

public class Client {
    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        System.out.println("a first line");
        for (int i=0; i < 10; i++) {
          HTMLElement div = document.createElement("div");
          div.appendChild(document.createTextNode("TeaVM generated element: " + i));
          document.getBody().appendChild(div);
        }
        XMLHttpRequest xhr = XMLHttpRequest.create();
        xhr.onComplete(() -> receiveResponse((ArrayBuffer) xhr.getResponse()));
        xhr.open("GET", "image");
        xhr.setResponseType("arraybuffer");
        xhr.send();
    }

    private static void receiveResponse(ArrayBuffer text) {
            Int8Array array = Int8Array.create(text);
            byte[] bytes = new byte[array.getLength()];
            for (int i = 0; i < bytes.length; ++i) {
                bytes[i] = array.get(i);
            }
        Runner runner = new Runner(new ByteArrayInputStream(bytes));
        runner.doIt("3 + 2");
        runner.doIt("((1 / 3) + (3 / 4)) printString");
    }
}

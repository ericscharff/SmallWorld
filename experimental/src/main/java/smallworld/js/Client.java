package smallworld.js;

import smallworld.core.Runner;
import java.io.ByteArrayInputStream;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.html.HTMLButtonElement;

public class Client {
    private static HTMLDocument document = HTMLDocument.current();
    private static HTMLButtonElement doItButton = document.getElementById("hello-button").cast();
    private static HTMLInputElement requestInput = document.getElementById("smalltalk").cast();
    private static HTMLElement responsePanel = document.getElementById("response-panel");
    private static Runner runner;

    public static void main(String[] args) {
        doItButton.listenClick(evt -> doIt());
        XMLHttpRequest xhr = XMLHttpRequest.create();

        xhr.onComplete(() -> receiveResponse((ArrayBuffer) xhr.getResponse()));
        xhr.open("GET", "WEB-INF/classes/image");
        xhr.setResponseType("arraybuffer");
        xhr.send();
    }

    private static void receiveResponse(ArrayBuffer text) {
            Int8Array array = Int8Array.create(text);
            byte[] bytes = new byte[array.getLength()];
            for (int i = 0; i < bytes.length; ++i) {
                bytes[i] = array.get(i);
            }
        runner = new Runner(new ByteArrayInputStream(bytes));
//        runner.doIt("3 + 2");
//        runner.doIt("((1 / 3) + (3 / 4)) printString");
    }

    private static void doIt() {
      String result = runner.doIt(requestInput.getValue()).toString();
        HTMLElement div = document.createElement("div");
        div.appendChild(document.createTextNode("Result: " + result));
        responsePanel.appendChild(div);
    }
}

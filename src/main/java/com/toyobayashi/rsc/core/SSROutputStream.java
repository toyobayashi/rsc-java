package com.toyobayashi.rsc.core;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SSROutputStream extends OutputStream {
  private OutputStream out;

  private ArrayList<Byte> buffer = new ArrayList<>();
  private String htmlCloseTag = "";

  /* private static String fromCharCode(int... codePoints) {
    StringBuilder builder = new StringBuilder(codePoints.length);
    for (int codePoint : codePoints) {
        builder.append(Character.toChars(codePoint));
    }
    return builder.toString();
  } */

  private static String createRSCPayloadScript(String script) throws IOException {
    return "<script>" + escapeScript(
      "(self.__rsc_payload = self.__rsc_payload || []).push(" + new ObjectMapper().writeValueAsString(script) + ");"
    ) + "</script>";
  }

  private static String escapeScript(String script) {
    String tmp = Pattern.compile("<!--").matcher(script).replaceAll("<\\!--");
    return Pattern.compile("</(script)").matcher(tmp).replaceAll("<\\$1");
  }

  public SSROutputStream(OutputStream out) {
    this.out = out;
  }

  @Override
  public void write(int b) throws IOException {
    buffer.add((byte) b);
  }

  @Override
  public void write(byte[] b) throws IOException {
    super.write(b);

    byte[] bytes = new byte[this.buffer.size()];
    for (int i = 0; i < this.buffer.size(); i++) {
      bytes[i] = this.buffer.get(i);
    }
    String str = new String(bytes, StandardCharsets.UTF_8);
    this.buffer.clear();

    if (this.htmlCloseTag != "</body></html>") {
      this.htmlCloseTag += str;
      if (str.length() >= 28) {
        int index = this.htmlCloseTag.indexOf("</body></html>");
        if (index != -1) {
          String front = this.htmlCloseTag.substring(0, index);
          String back = this.htmlCloseTag.substring(index + 14);
          this.htmlCloseTag = this.htmlCloseTag.substring(index, index + 14);
          this.out.write(front.getBytes(StandardCharsets.UTF_8));
          this.out.write(createRSCPayloadScript(back).getBytes(StandardCharsets.UTF_8));
        } else {
          String front = this.htmlCloseTag.substring(0, this.htmlCloseTag.length() - 28);
          this.htmlCloseTag = this.htmlCloseTag.substring(this.htmlCloseTag.length() - 28);
          this.out.write(front.getBytes(StandardCharsets.UTF_8));
        }
      }
    } else {
      this.out.write(createRSCPayloadScript(str).getBytes(StandardCharsets.UTF_8));
    }
    // String tmp = String.valueOf(buffer.toArray());
  }

  @Override
  public void flush() throws IOException {
    this.out.write(this.htmlCloseTag.getBytes(StandardCharsets.UTF_8));
  }
}

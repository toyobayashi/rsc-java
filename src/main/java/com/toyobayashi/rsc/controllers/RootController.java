package com.toyobayashi.rsc.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.toyobayashi.rsc.app.Index;
import com.toyobayashi.rsc.core.JSXElement;
import com.toyobayashi.rsc.core.PropsBuilder;
import com.toyobayashi.rsc.core.RenderUtil;
import com.toyobayashi.rsc.core.SSROutputStream;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class RootController implements ErrorController {
  private static final JSXElement[] ADDITIONAL_SCRIPTS = {
    JSXElement.h("script", PropsBuilder.empty()
      .put("type", "importmap")
      .put("children", """   
      {
        "imports": {
          "rsc:client-component": "/ClientComponent.js",
          "react": "https://esm.sh/react@19.0.0-rc-2d2cc042-20240809",
          "react-dom/client": "https://esm.sh/react-dom@19.0.0-rc-2d2cc042-20240809/client",
          "react-dom": "https://esm.sh/react-dom@19.0.0-rc-2d2cc042-20240809",
          "react/jsx-runtime": "https://esm.sh/react@19.0.0-rc-2d2cc042-20240809/jsx-runtime",
          "react-server-dom-webpack/client": "https://esm.sh/react-server-dom-webpack@19.0.0-rc-2d2cc042-20240809/client"
        }
      }
      """)
      .build()
    ),
    JSXElement.h("script", PropsBuilder.empty()
      .put("src", "/webpack-runtime-polyfill.js")
      .build()
    ),
    JSXElement.h("script", PropsBuilder.empty()
      .put("type", "module")
      .put("src", "/bootstrap.js")
      .build()
    )
  };

  @GetMapping("/")
	public ResponseEntity<StreamingResponseBody> indexPage(HttpServletRequest request) {
    JSXElement model = JSXElement.h(Index.class, PropsBuilder.empty()
      .put("scripts", ADDITIONAL_SCRIPTS)
      .put("timestamp", System.currentTimeMillis())
      .build());

    if (request.getParameter("rsc") != null) {
      StreamingResponseBody responseBody = (outputStream) -> {
        RenderUtil.renderToPayload(model, outputStream);
      };

      return ResponseEntity.ok()
        .header("Content-Type", "text/x-component")
        .body(responseBody);
    }

    StreamingResponseBody responseBody = (outputStream) -> {
      SSROutputStream stream = new SSROutputStream(outputStream);
      RenderUtil.renderToString(model, stream);
      RenderUtil.renderToPayload(model, stream);
      stream.flush();
    };
		return ResponseEntity.ok()
      .contentType(MediaType.TEXT_HTML)
      .body(responseBody);
	}
}

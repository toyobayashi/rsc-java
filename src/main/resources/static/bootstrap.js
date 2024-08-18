import * as React from 'react'
import * as ReactDOM from 'react-dom/client'
import * as ReactServerDomWebpack from 'react-server-dom-webpack/client'
import { jsx } from 'react/jsx-runtime'

let encoder = new TextEncoder();
let streamController;
const rscStream = new ReadableStream({
  start(controller) {
    if (typeof window === 'undefined') {
      return;
    }
    let handleChunk = chunk => {
      if (typeof chunk === 'string') {
        controller.enqueue(encoder.encode(chunk));
      } else {
        controller.enqueue(chunk);
      }
    };
    window.__rsc_payload = window.__rsc_payload || [];
    window.__rsc_payload.forEach(handleChunk);
    const originalPush = window.__rsc_payload.push;
    window.__rsc_payload.push = (chunk) => {
      const ret = originalPush.call(window.__rsc_payload, chunk);
      handleChunk(chunk);
      return ret
    };
    streamController = controller;
  },
});

if (typeof document !== 'undefined' && document.readyState === 'loading') {
  console.log('loading')
  document.addEventListener('DOMContentLoaded', () => {
    streamController?.close();
  });
} else {
  streamController?.close();
}

let data = ReactServerDomWebpack.createFromReadableStream(rscStream)
data.then(res => {
  console.log(res)
})
function Root () {
  return React.use(data)
}

React.startTransition(() => {
  ReactDOM.hydrateRoot(document, jsx(Root, {}))
})

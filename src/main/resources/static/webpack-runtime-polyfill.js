(function (exports) {
  // chunkId === moduleId

  const __webpack_module_cache__ = {}
  const __webpack_require__ = function (moduleId) {
 		var cachedModule = __webpack_module_cache__[moduleId]
 		if (cachedModule !== undefined) {
 			return cachedModule.exports
 		}
    throw new Error('Cannot find module "' + moduleId + '"')
  }

  __webpack_require__.u = function (chunkId) {
    return "" + chunkId + ".js";
  }
  __webpack_require__.p = '/'

  var installedChunks = {}

  const __webpack_chunk_load__ = __webpack_require__.e = function (chunkId) {
    const promises = []
    let installedChunkData = installedChunks[chunkId]

    if (installedChunkData !== 0) {
      if (installedChunkData) {
        promises.push(installedChunkData[2])
      } else {
        var promise = new Promise((resolve, reject) => (installedChunkData = installedChunks[chunkId] = [resolve, reject]));
        promises.push(installedChunkData[2] = promise);
        const url = __webpack_require__.p + __webpack_require__.u(chunkId)
        import(url).then((module) => {
          const mod = Object.create(null);
          Object.defineProperty(mod, '__esModule', { value: true });
          Object.defineProperty(mod, Symbol.toStringTag, { value: 'Module' });
          Object.keys(module).forEach(key => {
            Object.defineProperty(mod, key, {
              enumerable: true,
              get: () => module[key]
            })
          });
          Object.freeze(mod);
          installedChunkData[0](mod);
          installedChunks[chunkId] = 0;
          __webpack_module_cache__[chunkId] = { exports: mod };
        }, (err) => {
          installedChunkData[1](err);
          delete installedChunks[chunkId];
        })
      }
    }

    return Promise.all(promises)
  }

  exports.__webpack_require__ = __webpack_require__
  exports.__webpack_chunk_load__ = __webpack_chunk_load__
})(globalThis)

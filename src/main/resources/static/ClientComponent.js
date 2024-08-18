'use client'

import * as React from 'react'
import { jsx } from 'react/jsx-runtime'

export default function ClientComponent ({ is, ssrChildren, ...rest }) {
  const [C, setC] = React.useState(null)

  const promise = React.useMemo(() => {
    return __webpack_chunk_load__(is).then(() => {
      return __webpack_require__(is).default
    })
  }, [is])

  React.useEffect(() => {
    promise.then((C) => {
      setC(() => C)
    })
  }, [promise])

  return C ? jsx(C, rest) : ssrChildren
}

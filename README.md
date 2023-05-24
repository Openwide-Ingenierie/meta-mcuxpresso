# meta-mcuxpresso

This layer provides basic support for NXP MCUXpresso SDK, which is a
comprehensive software enablement package designed to simplify and accelerate
application development with NXP's microcontrollers based on Arm Cortex-M cores
and derivatives.

Also, this layer provides support for:
- FreeRTOS
- RPMsg Lite

## Dependencies

A gcc-arm-none-eabi toolchain is required to build the SDK. The toolchain
should be installed as a native toolchain in the build host. It must provide
a gcc-compatible 10+ version as a "virtual/arm-none-eabi-gcc" package.

A compatible toolchain can be downloaded from the ARM developer website or
from a layer such as [meta-arm](https://github.com/jonmason/meta-arm)

## Example

A recipe example is provided in the recipes-mcuxhelloworld directory. It builds
a simple hello world application for the integrated M4 on a NXP imx8mm SoC.

## Add the meta-mcuxpresso layer to your build

In order to use this layer, you need to make the build system aware of
it:
```
$ bitbake-layers add-layer meta-mcuxpresso
```

## License
This layer is licensed under the MIT License - see the [LICENSE](LICENSE)
file for details

# Add the MCUxpresso SDK to the SDK

RDEPENDS:${PN}:append = " \
    nativesdk-gcc-arm-none-eabi \
    nativesdk-mcuxpresso \
    nativesdk-mxfreertos \
    nativesdk-rpmsglite \
    nativesdk-python \
"

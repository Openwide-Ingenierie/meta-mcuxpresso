SUMMARY = "MCUXpresso FreeRTOS recipe"
DESCRIPTION = "Add Freertos to the MCUXpresso SDK"
LICENSE = "MIT"
LIC_FILES_CHKSUM ?= "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Clone from git repository
# V10.5.1
SRC_URI = "git://github.com/FreeRTOS/FreeRTOS-Kernel.git;protocol=https;branch=main"
# Get head
SRCREV = "${AUTOREV}"

PROVIDES = "virtual/mxfreertos"
BBCLASSEXTEND = "native nativesdk"

DEPENDS += "virtual/mcuxpresso"

S = "${WORKDIR}/git"

# Install the SDK to the sysroot-native in /usr/include/mcuxpresso
do_install() {
    # FreeRTOS tags are under a detached head, so we need to checkout the
    # Tag to get the correct version as Yocto doesn't support detached heads
    cd ${S};git checkout V${PV}

    # Create the include directory for the SDK
    install -d ${D}${includedir}/mcux-sdk/rtos/freertos/freertos-kernel

    # Copy the mcuxpresso SDK root to the include directory
    cp -r ${S}/* ${D}${includedir}/mcux-sdk/rtos/freertos/freertos-kernel
}

do_install:append:class-nativesdk() {

    cd ${S};git checkout V${PV}

    # Create the include directory for the SDK
    install -d ${D}${base_prefix}/mcux-sdk/rtos/freertos/freertos-kernel

    # Copy the mcuxpresso SDK root to the include directory
    cp -r ${S}/* ${D}${base_prefix}/mcux-sdk/rtos/freertos/freertos-kernel
}

FILES:${PN} += "${includedir}/mcux-sdk/rtos/freertos/freertos-kernel/"
FILES:${PN}:append:class-nativesdk = " ${base_prefix}/mcux-sdk/rtos/freertos/freertos-kernel/"

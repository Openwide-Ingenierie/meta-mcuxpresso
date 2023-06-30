SUMMARY = "MCUXpresso RPMsg lite recipe"
DESCRIPTION = "Add RPMsglite to the MCUXpresso SDK"
LICENSE = "MIT"
LIC_FILES_CHKSUM ?= "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

BBCLASSEXTEND = "native nativesdk"

# Clone from git repository
SRC_URI = "git://github.com/nxp-mcuxpresso/rpmsg-lite.git;protocol=https;branch=master"
SRCREV = "c95382aa688b0c995366d2dc2182b96a34c0c5a4"

PROVIDES = "virtual/rpmsglite"
DEPENDS += "virtual/mcuxpresso"

S = "${WORKDIR}/git"

# Install the SDK to the sysroot-native in /usr/include/mcuxpresso
do_install() {
    # Create the include directory for the SDK
    install -d ${D}${includedir}/mcux-sdk/middleware/rpmsg_lite

    # Copy the mcuxpresso SDK root to the include directory
    cp -r ${S}/* ${D}${includedir}/mcux-sdk/middleware/rpmsg_lite
}

do_install:append:class-nativesdk() {
    install -d ${D}${base_prefix}/mcux-sdk/middleware/rpmsg_lite
    cp -r ${S}/* ${D}${base_prefix}/mcux-sdk/middleware/rpmsg_lite
}

FILES:${PN} += "${includedir}/mcux-sdk/middleware/rpmsg_lite/"
FILES:${PN}:append:class-nativesdk = " ${base_prefix}/mcux-sdk/middleware/rpmsg_lite/"

SUMMARY = "mcuxhelloworld"
DESCRIPTION = "M4 firmware Hello World from Yocto"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy cmake

DEPENDS += "mcuxpresso-native"
DEPENDS += "gcc-arm-none-eabi-native"

S = "${WORKDIR}"

SRC_URI = " \
        file://CMakeLists.txt \
        file://config.cmake \
        file://empty_rsc_table.c \
        file://flags.cmake \
        file://fsl_iomuxc.h \
        file://main.c \
        file://MIMX8MM6xxxxx_cm4_ddr_ram.ld \
        file://MIMX8MM6xxxxx_cm4_flash.ld \
        file://MIMX8MM6xxxxx_cm4_ram.ld \
        file://pin_mux.c \
        file://pin_mux.h \
        file://readme.txt \
"

# Override the default compiler to use the cross compiler instead
CC = "arm-none-eabi-gcc"
CXX = "arm-none-eabi-g++"
AS = "arm-none-eabi-as"

# Get MCUXPRESSO SDK and CMAKE toolchain file
MCUXPRESSO_SDK = "${STAGING_DIR_NATIVE}/usr/include/mcux-sdk"
MCUXPRESSO_CMAKE_TOOLCHAIN = "${MCUXPRESSO_SDK}/tools/cmake_toolchain_files/armgcc.cmake"

do_configure() {
        export MCUXPRESSO_SDK="${STAGING_DIR_NATIVE}/usr/include/mcux-sdk"
        export ARMGCC_DIR=${STAGING_DIR_NATIVE}/usr

        cmake -DCMAKE_TOOLCHAIN_FILE=${MCUXPRESSO_CMAKE_TOOLCHAIN} \
                -DCMAKE_BUILD_TYPE=release \
                -DCMAKE_C_COMPILER=${CC} \
                -DCMAKE_CXX_COMPILER=${CXX} \
                -DCMAKE_ASM_COMPILER=${AS} \
                -DCMAKE_C_COMPILER_WORKS=false \
                -DCMAKE_MAKE_PROGRAM=${MAKE} \
                ${S}
}

do_install() {
        install -d ${D}/etc/firmware/hello_world.bin
        install -m 0644 ${S}/release/hello_world.bin ${D}/etc/firmware/hello_world.bin
}

# Uboot expects the firmware to be in the deploy directory, it will then
# copy it to the correct location
do_deploy() {
        install -d ${DEPLOYDIR}
        install -m 0644 ${S}/release/hello_world.bin ${DEPLOYDIR}/hello_world.bin
}

addtask deploy after do_install

# Skip architecture check as we are building for a different architecture
INSANE_SKIP:${PN} += "arch"

# Remove building a debug package, we're going to extract a raw binary which
# doesn't export any symbols
PACKAGES = "${PN}"

FILES:${PN} += "${DEPLOYDIR}/hello_world.bin \
                /etc/firmware/hello_world.bin"
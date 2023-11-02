
build/dhrystone.elf:     file format elf32-littleriscv


Disassembly of section .init:

80000000 <_start>:
80000000:	00002197          	auipc	gp,0x2
80000004:	00018193          	mv	gp,gp

80000008 <init>:
80000008:	00005117          	auipc	sp,0x5
8000000c:	01810113          	addi	sp,sp,24 # 80005020 <__freertos_irq_stack_top>
80000010:	00001517          	auipc	a0,0x1
80000014:	20450513          	addi	a0,a0,516 # 80001214 <__init_array_end>
80000018:	00001597          	auipc	a1,0x1
8000001c:	1fc58593          	addi	a1,a1,508 # 80001214 <__init_array_end>
80000020:	81418613          	addi	a2,gp,-2028 # 80001814 <Dhrystones_Per_Second>
80000024:	00c5fc63          	bgeu	a1,a2,8000003c <init+0x34>
80000028:	00052283          	lw	t0,0(a0)
8000002c:	0055a023          	sw	t0,0(a1)
80000030:	00450513          	addi	a0,a0,4
80000034:	00458593          	addi	a1,a1,4
80000038:	fec5e8e3          	bltu	a1,a2,80000028 <init+0x20>
8000003c:	81418513          	addi	a0,gp,-2028 # 80001814 <Dhrystones_Per_Second>
80000040:	00004597          	auipc	a1,0x4
80000044:	fe058593          	addi	a1,a1,-32 # 80004020 <_end>
80000048:	00b57863          	bgeu	a0,a1,80000058 <init+0x50>
8000004c:	00052023          	sw	zero,0(a0)
80000050:	00450513          	addi	a0,a0,4
80000054:	feb56ce3          	bltu	a0,a1,8000004c <init+0x44>
80000058:	7ad000ef          	jal	ra,80001004 <__libc_init_array>
8000005c:	000060b7          	lui	ra,0x6
80000060:	3000b073          	csrc	mstatus,ra
80000064:	000020b7          	lui	ra,0x2
80000068:	3000a073          	csrs	mstatus,ra
8000006c:	018000ef          	jal	ra,80000084 <main>

80000070 <pass>:
80000070:	0000006f          	j	80000070 <pass>
80000074:	00000013          	nop

80000078 <fail>:
80000078:	0000006f          	j	80000078 <fail>
8000007c:	00000013          	nop

80000080 <_init>:
80000080:	00008067          	ret

Disassembly of section .text:

80000084 <main>:
80000084:	f6010113          	addi	sp,sp,-160
80000088:	08112e23          	sw	ra,156(sp)
8000008c:	08812c23          	sw	s0,152(sp)
80000090:	08912a23          	sw	s1,148(sp)
80000094:	0a010413          	addi	s0,sp,160
80000098:	09212823          	sw	s2,144(sp)
8000009c:	09312623          	sw	s3,140(sp)
800000a0:	09412423          	sw	s4,136(sp)
800000a4:	09512223          	sw	s5,132(sp)
800000a8:	09612023          	sw	s6,128(sp)
800000ac:	07712e23          	sw	s7,124(sp)
800000b0:	07812c23          	sw	s8,120(sp)
800000b4:	07912a23          	sw	s9,116(sp)
800000b8:	07a12823          	sw	s10,112(sp)
800000bc:	07b12623          	sw	s11,108(sp)
800000c0:	fc010113          	addi	sp,sp,-64
800000c4:	00001717          	auipc	a4,0x1
800000c8:	15070713          	addi	a4,a4,336 # 80001214 <__init_array_end>
800000cc:	00f10613          	addi	a2,sp,15
800000d0:	fc010113          	addi	sp,sp,-64
800000d4:	00001797          	auipc	a5,0x1
800000d8:	6d878793          	addi	a5,a5,1752 # 800017ac <__init_array_end+0x598>
800000dc:	00872f83          	lw	t6,8(a4)
800000e0:	00c72f03          	lw	t5,12(a4)
800000e4:	01072e83          	lw	t4,16(a4)
800000e8:	01472e03          	lw	t3,20(a4)
800000ec:	01872303          	lw	t1,24(a4)
800000f0:	00072683          	lw	a3,0(a4)
800000f4:	00472283          	lw	t0,4(a4)
800000f8:	01c75883          	lhu	a7,28(a4)
800000fc:	01e74803          	lbu	a6,30(a4)
80000100:	00f10713          	addi	a4,sp,15
80000104:	ff077713          	andi	a4,a4,-16
80000108:	0007a583          	lw	a1,0(a5)
8000010c:	00200393          	li	t2,2
80000110:	ff067613          	andi	a2,a2,-16
80000114:	00772423          	sw	t2,8(a4)
80000118:	02800393          	li	t2,40
8000011c:	00772623          	sw	t2,12(a4)
80000120:	00c72023          	sw	a2,0(a4)
80000124:	00d72823          	sw	a3,16(a4)
80000128:	82c1ae23          	sw	a2,-1988(gp) # 8000183c <Next_Ptr_Glob>
8000012c:	00072223          	sw	zero,4(a4)
80000130:	84e1a023          	sw	a4,-1984(gp) # 80001840 <Ptr_Glob>
80000134:	0047a503          	lw	a0,4(a5)
80000138:	00572a23          	sw	t0,20(a4)
8000013c:	0147a603          	lw	a2,20(a5)
80000140:	0187a683          	lw	a3,24(a5)
80000144:	01f72c23          	sw	t6,24(a4)
80000148:	01e72e23          	sw	t5,28(a4)
8000014c:	03d72023          	sw	t4,32(a4)
80000150:	03c72223          	sw	t3,36(a4)
80000154:	02672423          	sw	t1,40(a4)
80000158:	03171623          	sh	a7,44(a4)
8000015c:	03070723          	sb	a6,46(a4)
80000160:	0087a883          	lw	a7,8(a5)
80000164:	01c7d703          	lhu	a4,28(a5)
80000168:	00c7a803          	lw	a6,12(a5)
8000016c:	f8b42023          	sw	a1,-128(s0)
80000170:	0107a583          	lw	a1,16(a5)
80000174:	01e7c783          	lbu	a5,30(a5)
80000178:	f8a42223          	sw	a0,-124(s0)
8000017c:	00a00513          	li	a0,10
80000180:	f8f40f23          	sb	a5,-98(s0)
80000184:	00a00793          	li	a5,10
80000188:	f8e41e23          	sh	a4,-100(s0)
8000018c:	f9142423          	sw	a7,-120(s0)
80000190:	00002717          	auipc	a4,0x2
80000194:	dcf72c23          	sw	a5,-552(a4) # 80001f68 <Arr_2_Glob+0x65c>
80000198:	f9042623          	sw	a6,-116(s0)
8000019c:	f8b42823          	sw	a1,-112(s0)
800001a0:	f8c42a23          	sw	a2,-108(s0)
800001a4:	f8d42c23          	sw	a3,-104(s0)
800001a8:	778000ef          	jal	ra,80000920 <putchar>
800001ac:	00001597          	auipc	a1,0x1
800001b0:	08858593          	addi	a1,a1,136 # 80001234 <__init_array_end+0x20>
800001b4:	00001517          	auipc	a0,0x1
800001b8:	09050513          	addi	a0,a0,144 # 80001244 <__init_array_end+0x30>
800001bc:	025000ef          	jal	ra,800009e0 <printf>
800001c0:	82c18793          	addi	a5,gp,-2004 # 8000182c <Reg>
800001c4:	0007a783          	lw	a5,0(a5)
800001c8:	6e078863          	beqz	a5,800008b8 <main+0x834>
800001cc:	00001517          	auipc	a0,0x1
800001d0:	09c50513          	addi	a0,a0,156 # 80001268 <__init_array_end+0x54>
800001d4:	111000ef          	jal	ra,80000ae4 <puts>
800001d8:	00b72637          	lui	a2,0xb72
800001dc:	b0060613          	addi	a2,a2,-1280 # b71b00 <__stack_size+0xb70b00>
800001e0:	00000693          	li	a3,0
800001e4:	00001597          	auipc	a1,0x1
800001e8:	0e058593          	addi	a1,a1,224 # 800012c4 <__init_array_end+0xb0>
800001ec:	00001517          	auipc	a0,0x1
800001f0:	0e050513          	addi	a0,a0,224 # 800012cc <__init_array_end+0xb8>
800001f4:	7ec000ef          	jal	ra,800009e0 <printf>
800001f8:	00a00513          	li	a0,10
800001fc:	724000ef          	jal	ra,80000920 <putchar>
80000200:	1f400793          	li	a5,500
80000204:	f6f42623          	sw	a5,-148(s0)
80000208:	00001997          	auipc	s3,0x1
8000020c:	5c498993          	addi	s3,s3,1476 # 800017cc <__init_array_end+0x5b8>
80000210:	8201a423          	sw	zero,-2008(gp) # 80001828 <Done>
80000214:	84018d13          	addi	s10,gp,-1984 # 80001840 <Ptr_Glob>
80000218:	00001d97          	auipc	s11,0x1
8000021c:	6f4d8d93          	addi	s11,s11,1780 # 8000190c <Arr_2_Glob>
80000220:	00001497          	auipc	s1,0x1
80000224:	5cc48493          	addi	s1,s1,1484 # 800017ec <__init_array_end+0x5d8>
80000228:	f6c42903          	lw	s2,-148(s0)
8000022c:	00001517          	auipc	a0,0x1
80000230:	0b450513          	addi	a0,a0,180 # 800012e0 <__init_array_end+0xcc>
80000234:	00100a13          	li	s4,1
80000238:	00090593          	mv	a1,s2
8000023c:	7a4000ef          	jal	ra,800009e0 <printf>
80000240:	00100513          	li	a0,1
80000244:	6d4000ef          	jal	ra,80000918 <setStats>
80000248:	00000513          	li	a0,0
8000024c:	6d0000ef          	jal	ra,8000091c <time>
80000250:	00190c93          	addi	s9,s2,1
80000254:	82a1a223          	sw	a0,-2012(gp) # 80001824 <Begin_Time>
80000258:	00200913          	li	s2,2
8000025c:	00100c13          	li	s8,1
80000260:	595000ef          	jal	ra,80000ff4 <Proc_5>
80000264:	569000ef          	jal	ra,80000fcc <Proc_4>
80000268:	0149a603          	lw	a2,20(s3)
8000026c:	01e9c783          	lbu	a5,30(s3)
80000270:	0009ae83          	lw	t4,0(s3)
80000274:	0049ae03          	lw	t3,4(s3)
80000278:	0089a303          	lw	t1,8(s3)
8000027c:	00c9a883          	lw	a7,12(s3)
80000280:	0109a803          	lw	a6,16(s3)
80000284:	0189a683          	lw	a3,24(s3)
80000288:	01c9d703          	lhu	a4,28(s3)
8000028c:	fa040593          	addi	a1,s0,-96
80000290:	f8040513          	addi	a0,s0,-128
80000294:	fac42a23          	sw	a2,-76(s0)
80000298:	faf40f23          	sb	a5,-66(s0)
8000029c:	f7242a23          	sw	s2,-140(s0)
800002a0:	fbd42023          	sw	t4,-96(s0)
800002a4:	fbc42223          	sw	t3,-92(s0)
800002a8:	fa642423          	sw	t1,-88(s0)
800002ac:	fb142623          	sw	a7,-84(s0)
800002b0:	fb042823          	sw	a6,-80(s0)
800002b4:	fad42c23          	sw	a3,-72(s0)
800002b8:	fae41e23          	sh	a4,-68(s0)
800002bc:	f7842e23          	sw	s8,-132(s0)
800002c0:	23d000ef          	jal	ra,80000cfc <Func_2>
800002c4:	f7442603          	lw	a2,-140(s0)
800002c8:	00153513          	seqz	a0,a0
800002cc:	82a1aa23          	sw	a0,-1996(gp) # 80001834 <Bool_Glob>
800002d0:	02c94a63          	blt	s2,a2,80000304 <main+0x280>
800002d4:	00261793          	slli	a5,a2,0x2
800002d8:	00c787b3          	add	a5,a5,a2
800002dc:	ffd78793          	addi	a5,a5,-3
800002e0:	00060513          	mv	a0,a2
800002e4:	00300593          	li	a1,3
800002e8:	f7840613          	addi	a2,s0,-136
800002ec:	f6f42c23          	sw	a5,-136(s0)
800002f0:	179000ef          	jal	ra,80000c68 <Proc_7>
800002f4:	f7442603          	lw	a2,-140(s0)
800002f8:	00160613          	addi	a2,a2,1
800002fc:	f6c42a23          	sw	a2,-140(s0)
80000300:	fcc95ae3          	bge	s2,a2,800002d4 <main+0x250>
80000304:	f7842683          	lw	a3,-136(s0)
80000308:	000d8593          	mv	a1,s11
8000030c:	84418513          	addi	a0,gp,-1980 # 80001844 <Arr_1_Glob>
80000310:	169000ef          	jal	ra,80000c78 <Proc_8>
80000314:	000d2503          	lw	a0,0(s10)
80000318:	04100b13          	li	s6,65
8000031c:	00300a93          	li	s5,3
80000320:	34d000ef          	jal	ra,80000e6c <Proc_1>
80000324:	8301c703          	lbu	a4,-2000(gp) # 80001830 <Ch_2_Glob>
80000328:	04000793          	li	a5,64
8000032c:	02e7f463          	bgeu	a5,a4,80000354 <main+0x2d0>
80000330:	000b0513          	mv	a0,s6
80000334:	04300593          	li	a1,67
80000338:	1a5000ef          	jal	ra,80000cdc <Func_1>
8000033c:	f7c42783          	lw	a5,-132(s0)
80000340:	001b0713          	addi	a4,s6,1
80000344:	4cf50663          	beq	a0,a5,80000810 <main+0x78c>
80000348:	0ff77b13          	andi	s6,a4,255
8000034c:	8301c783          	lbu	a5,-2000(gp) # 80001830 <Ch_2_Glob>
80000350:	ff67f0e3          	bgeu	a5,s6,80000330 <main+0x2ac>
80000354:	f7442783          	lw	a5,-140(s0)
80000358:	f7842b83          	lw	s7,-136(s0)
8000035c:	001a0a13          	addi	s4,s4,1
80000360:	02fa8ab3          	mul	s5,s5,a5
80000364:	f7440513          	addi	a0,s0,-140
80000368:	037acb33          	div	s6,s5,s7
8000036c:	f7642a23          	sw	s6,-140(s0)
80000370:	2a5000ef          	jal	ra,80000e14 <Proc_2>
80000374:	ef9a16e3          	bne	s4,s9,80000260 <main+0x1dc>
80000378:	00000513          	li	a0,0
8000037c:	5a0000ef          	jal	ra,8000091c <time>
80000380:	82a1a023          	sw	a0,-2016(gp) # 80001820 <End_Time>
80000384:	00000513          	li	a0,0
80000388:	590000ef          	jal	ra,80000918 <setStats>
8000038c:	82418713          	addi	a4,gp,-2012 # 80001824 <Begin_Time>
80000390:	82018793          	addi	a5,gp,-2016 # 80001820 <End_Time>
80000394:	00072683          	lw	a3,0(a4)
80000398:	0007a783          	lw	a5,0(a5)
8000039c:	00018737          	lui	a4,0x18
800003a0:	69f70713          	addi	a4,a4,1695 # 1869f <__stack_size+0x1769f>
800003a4:	40d787b3          	sub	a5,a5,a3
800003a8:	80f1ae23          	sw	a5,-2020(gp) # 8000181c <User_Time>
800003ac:	4cf75a63          	bge	a4,a5,80000880 <main+0x7fc>
800003b0:	00100793          	li	a5,1
800003b4:	82f1a423          	sw	a5,-2008(gp) # 80001828 <Done>
800003b8:	00001517          	auipc	a0,0x1
800003bc:	f8450513          	addi	a0,a0,-124 # 8000133c <__init_array_end+0x128>
800003c0:	724000ef          	jal	ra,80000ae4 <puts>
800003c4:	00a00513          	li	a0,10
800003c8:	558000ef          	jal	ra,80000920 <putchar>
800003cc:	83818793          	addi	a5,gp,-1992 # 80001838 <Int_Glob>
800003d0:	0007a583          	lw	a1,0(a5)
800003d4:	00001517          	auipc	a0,0x1
800003d8:	fa050513          	addi	a0,a0,-96 # 80001374 <__init_array_end+0x160>
800003dc:	83c18493          	addi	s1,gp,-1988 # 8000183c <Next_Ptr_Glob>
800003e0:	600000ef          	jal	ra,800009e0 <printf>
800003e4:	00500593          	li	a1,5
800003e8:	00001517          	auipc	a0,0x1
800003ec:	fa850513          	addi	a0,a0,-88 # 80001390 <__init_array_end+0x17c>
800003f0:	5f0000ef          	jal	ra,800009e0 <printf>
800003f4:	83418793          	addi	a5,gp,-1996 # 80001834 <Bool_Glob>
800003f8:	0007a583          	lw	a1,0(a5)
800003fc:	00001517          	auipc	a0,0x1
80000400:	fb050513          	addi	a0,a0,-80 # 800013ac <__init_array_end+0x198>
80000404:	417a8ab3          	sub	s5,s5,s7
80000408:	5d8000ef          	jal	ra,800009e0 <printf>
8000040c:	00100593          	li	a1,1
80000410:	00001517          	auipc	a0,0x1
80000414:	f8050513          	addi	a0,a0,-128 # 80001390 <__init_array_end+0x17c>
80000418:	5c8000ef          	jal	ra,800009e0 <printf>
8000041c:	8311c583          	lbu	a1,-1999(gp) # 80001831 <Ch_1_Glob>
80000420:	00001517          	auipc	a0,0x1
80000424:	fa850513          	addi	a0,a0,-88 # 800013c8 <__init_array_end+0x1b4>
80000428:	5b8000ef          	jal	ra,800009e0 <printf>
8000042c:	04100593          	li	a1,65
80000430:	00001517          	auipc	a0,0x1
80000434:	fb450513          	addi	a0,a0,-76 # 800013e4 <__init_array_end+0x1d0>
80000438:	5a8000ef          	jal	ra,800009e0 <printf>
8000043c:	8301c583          	lbu	a1,-2000(gp) # 80001830 <Ch_2_Glob>
80000440:	00001517          	auipc	a0,0x1
80000444:	fc050513          	addi	a0,a0,-64 # 80001400 <__init_array_end+0x1ec>
80000448:	598000ef          	jal	ra,800009e0 <printf>
8000044c:	04200593          	li	a1,66
80000450:	00001517          	auipc	a0,0x1
80000454:	f9450513          	addi	a0,a0,-108 # 800013e4 <__init_array_end+0x1d0>
80000458:	588000ef          	jal	ra,800009e0 <printf>
8000045c:	84418793          	addi	a5,gp,-1980 # 80001844 <Arr_1_Glob>
80000460:	0207a583          	lw	a1,32(a5)
80000464:	00001517          	auipc	a0,0x1
80000468:	fb850513          	addi	a0,a0,-72 # 8000141c <__init_array_end+0x208>
8000046c:	81c18913          	addi	s2,gp,-2020 # 8000181c <User_Time>
80000470:	570000ef          	jal	ra,800009e0 <printf>
80000474:	00700593          	li	a1,7
80000478:	00001517          	auipc	a0,0x1
8000047c:	f1850513          	addi	a0,a0,-232 # 80001390 <__init_array_end+0x17c>
80000480:	560000ef          	jal	ra,800009e0 <printf>
80000484:	00001797          	auipc	a5,0x1
80000488:	48878793          	addi	a5,a5,1160 # 8000190c <Arr_2_Glob>
8000048c:	65c7a583          	lw	a1,1628(a5)
80000490:	00001517          	auipc	a0,0x1
80000494:	fa850513          	addi	a0,a0,-88 # 80001438 <__init_array_end+0x224>
80000498:	548000ef          	jal	ra,800009e0 <printf>
8000049c:	00001517          	auipc	a0,0x1
800004a0:	fb850513          	addi	a0,a0,-72 # 80001454 <__init_array_end+0x240>
800004a4:	640000ef          	jal	ra,80000ae4 <puts>
800004a8:	00001517          	auipc	a0,0x1
800004ac:	fd850513          	addi	a0,a0,-40 # 80001480 <__init_array_end+0x26c>
800004b0:	634000ef          	jal	ra,80000ae4 <puts>
800004b4:	000d2783          	lw	a5,0(s10)
800004b8:	00001517          	auipc	a0,0x1
800004bc:	fd450513          	addi	a0,a0,-44 # 8000148c <__init_array_end+0x278>
800004c0:	0007a583          	lw	a1,0(a5)
800004c4:	51c000ef          	jal	ra,800009e0 <printf>
800004c8:	00001517          	auipc	a0,0x1
800004cc:	fe050513          	addi	a0,a0,-32 # 800014a8 <__init_array_end+0x294>
800004d0:	614000ef          	jal	ra,80000ae4 <puts>
800004d4:	000d2783          	lw	a5,0(s10)
800004d8:	00001517          	auipc	a0,0x1
800004dc:	00050513          	mv	a0,a0
800004e0:	0047a583          	lw	a1,4(a5)
800004e4:	4fc000ef          	jal	ra,800009e0 <printf>
800004e8:	00000593          	li	a1,0
800004ec:	00001517          	auipc	a0,0x1
800004f0:	ea450513          	addi	a0,a0,-348 # 80001390 <__init_array_end+0x17c>
800004f4:	4ec000ef          	jal	ra,800009e0 <printf>
800004f8:	000d2783          	lw	a5,0(s10)
800004fc:	00001517          	auipc	a0,0x1
80000500:	ff850513          	addi	a0,a0,-8 # 800014f4 <__init_array_end+0x2e0>
80000504:	0087a583          	lw	a1,8(a5)
80000508:	4d8000ef          	jal	ra,800009e0 <printf>
8000050c:	00200593          	li	a1,2
80000510:	00001517          	auipc	a0,0x1
80000514:	e8050513          	addi	a0,a0,-384 # 80001390 <__init_array_end+0x17c>
80000518:	4c8000ef          	jal	ra,800009e0 <printf>
8000051c:	000d2783          	lw	a5,0(s10)
80000520:	00001517          	auipc	a0,0x1
80000524:	ff050513          	addi	a0,a0,-16 # 80001510 <__init_array_end+0x2fc>
80000528:	00c7a583          	lw	a1,12(a5)
8000052c:	4b4000ef          	jal	ra,800009e0 <printf>
80000530:	01100593          	li	a1,17
80000534:	00001517          	auipc	a0,0x1
80000538:	e5c50513          	addi	a0,a0,-420 # 80001390 <__init_array_end+0x17c>
8000053c:	4a4000ef          	jal	ra,800009e0 <printf>
80000540:	000d2583          	lw	a1,0(s10)
80000544:	00001517          	auipc	a0,0x1
80000548:	fe850513          	addi	a0,a0,-24 # 8000152c <__init_array_end+0x318>
8000054c:	01058593          	addi	a1,a1,16
80000550:	490000ef          	jal	ra,800009e0 <printf>
80000554:	00001517          	auipc	a0,0x1
80000558:	ff450513          	addi	a0,a0,-12 # 80001548 <__init_array_end+0x334>
8000055c:	588000ef          	jal	ra,80000ae4 <puts>
80000560:	00001517          	auipc	a0,0x1
80000564:	01c50513          	addi	a0,a0,28 # 8000157c <__init_array_end+0x368>
80000568:	57c000ef          	jal	ra,80000ae4 <puts>
8000056c:	0004a783          	lw	a5,0(s1)
80000570:	00001517          	auipc	a0,0x1
80000574:	f1c50513          	addi	a0,a0,-228 # 8000148c <__init_array_end+0x278>
80000578:	0007a583          	lw	a1,0(a5)
8000057c:	464000ef          	jal	ra,800009e0 <printf>
80000580:	00001517          	auipc	a0,0x1
80000584:	00c50513          	addi	a0,a0,12 # 8000158c <__init_array_end+0x378>
80000588:	55c000ef          	jal	ra,80000ae4 <puts>
8000058c:	0004a783          	lw	a5,0(s1)
80000590:	00001517          	auipc	a0,0x1
80000594:	f4850513          	addi	a0,a0,-184 # 800014d8 <__init_array_end+0x2c4>
80000598:	0047a583          	lw	a1,4(a5)
8000059c:	444000ef          	jal	ra,800009e0 <printf>
800005a0:	00000593          	li	a1,0
800005a4:	00001517          	auipc	a0,0x1
800005a8:	dec50513          	addi	a0,a0,-532 # 80001390 <__init_array_end+0x17c>
800005ac:	434000ef          	jal	ra,800009e0 <printf>
800005b0:	0004a783          	lw	a5,0(s1)
800005b4:	00001517          	auipc	a0,0x1
800005b8:	f4050513          	addi	a0,a0,-192 # 800014f4 <__init_array_end+0x2e0>
800005bc:	0087a583          	lw	a1,8(a5)
800005c0:	420000ef          	jal	ra,800009e0 <printf>
800005c4:	00100593          	li	a1,1
800005c8:	00001517          	auipc	a0,0x1
800005cc:	dc850513          	addi	a0,a0,-568 # 80001390 <__init_array_end+0x17c>
800005d0:	410000ef          	jal	ra,800009e0 <printf>
800005d4:	0004a783          	lw	a5,0(s1)
800005d8:	00001517          	auipc	a0,0x1
800005dc:	f3850513          	addi	a0,a0,-200 # 80001510 <__init_array_end+0x2fc>
800005e0:	00c7a583          	lw	a1,12(a5)
800005e4:	3fc000ef          	jal	ra,800009e0 <printf>
800005e8:	01200593          	li	a1,18
800005ec:	00001517          	auipc	a0,0x1
800005f0:	da450513          	addi	a0,a0,-604 # 80001390 <__init_array_end+0x17c>
800005f4:	3ec000ef          	jal	ra,800009e0 <printf>
800005f8:	0004a583          	lw	a1,0(s1)
800005fc:	00001517          	auipc	a0,0x1
80000600:	f3050513          	addi	a0,a0,-208 # 8000152c <__init_array_end+0x318>
80000604:	00b724b7          	lui	s1,0xb72
80000608:	01058593          	addi	a1,a1,16
8000060c:	3d4000ef          	jal	ra,800009e0 <printf>
80000610:	00001517          	auipc	a0,0x1
80000614:	f3850513          	addi	a0,a0,-200 # 80001548 <__init_array_end+0x334>
80000618:	4cc000ef          	jal	ra,80000ae4 <puts>
8000061c:	f7442583          	lw	a1,-140(s0)
80000620:	00001517          	auipc	a0,0x1
80000624:	fac50513          	addi	a0,a0,-84 # 800015cc <__init_array_end+0x3b8>
80000628:	b0048493          	addi	s1,s1,-1280 # b71b00 <__stack_size+0xb70b00>
8000062c:	3b4000ef          	jal	ra,800009e0 <printf>
80000630:	00500593          	li	a1,5
80000634:	00001517          	auipc	a0,0x1
80000638:	d5c50513          	addi	a0,a0,-676 # 80001390 <__init_array_end+0x17c>
8000063c:	3a4000ef          	jal	ra,800009e0 <printf>
80000640:	003a9793          	slli	a5,s5,0x3
80000644:	41578ab3          	sub	s5,a5,s5
80000648:	416a85b3          	sub	a1,s5,s6
8000064c:	00001517          	auipc	a0,0x1
80000650:	f9c50513          	addi	a0,a0,-100 # 800015e8 <__init_array_end+0x3d4>
80000654:	38c000ef          	jal	ra,800009e0 <printf>
80000658:	00d00593          	li	a1,13
8000065c:	00001517          	auipc	a0,0x1
80000660:	d3450513          	addi	a0,a0,-716 # 80001390 <__init_array_end+0x17c>
80000664:	37c000ef          	jal	ra,800009e0 <printf>
80000668:	f7842583          	lw	a1,-136(s0)
8000066c:	00001517          	auipc	a0,0x1
80000670:	f9850513          	addi	a0,a0,-104 # 80001604 <__init_array_end+0x3f0>
80000674:	36c000ef          	jal	ra,800009e0 <printf>
80000678:	00700593          	li	a1,7
8000067c:	00001517          	auipc	a0,0x1
80000680:	d1450513          	addi	a0,a0,-748 # 80001390 <__init_array_end+0x17c>
80000684:	35c000ef          	jal	ra,800009e0 <printf>
80000688:	f7c42583          	lw	a1,-132(s0)
8000068c:	00001517          	auipc	a0,0x1
80000690:	f9450513          	addi	a0,a0,-108 # 80001620 <__init_array_end+0x40c>
80000694:	34c000ef          	jal	ra,800009e0 <printf>
80000698:	00100593          	li	a1,1
8000069c:	00001517          	auipc	a0,0x1
800006a0:	cf450513          	addi	a0,a0,-780 # 80001390 <__init_array_end+0x17c>
800006a4:	33c000ef          	jal	ra,800009e0 <printf>
800006a8:	f8040593          	addi	a1,s0,-128
800006ac:	00001517          	auipc	a0,0x1
800006b0:	f9050513          	addi	a0,a0,-112 # 8000163c <__init_array_end+0x428>
800006b4:	32c000ef          	jal	ra,800009e0 <printf>
800006b8:	00001517          	auipc	a0,0x1
800006bc:	fa050513          	addi	a0,a0,-96 # 80001658 <__init_array_end+0x444>
800006c0:	424000ef          	jal	ra,80000ae4 <puts>
800006c4:	fa040593          	addi	a1,s0,-96
800006c8:	00001517          	auipc	a0,0x1
800006cc:	fc450513          	addi	a0,a0,-60 # 8000168c <__init_array_end+0x478>
800006d0:	310000ef          	jal	ra,800009e0 <printf>
800006d4:	00001517          	auipc	a0,0x1
800006d8:	fd450513          	addi	a0,a0,-44 # 800016a8 <__init_array_end+0x494>
800006dc:	408000ef          	jal	ra,80000ae4 <puts>
800006e0:	00a00513          	li	a0,10
800006e4:	23c000ef          	jal	ra,80000920 <putchar>
800006e8:	00092603          	lw	a2,0(s2)
800006ec:	f6c42a03          	lw	s4,-148(s0)
800006f0:	00c00793          	li	a5,12
800006f4:	41f65693          	srai	a3,a2,0x1f
800006f8:	034649b3          	div	s3,a2,s4
800006fc:	02f9c9b3          	div	s3,s3,a5
80000700:	029a15b3          	mulh	a1,s4,s1
80000704:	8131ac23          	sw	s3,-2024(gp) # 80001818 <Microseconds>
80000708:	029a0533          	mul	a0,s4,s1
8000070c:	41c000ef          	jal	ra,80000b28 <__divdi3>
80000710:	80a1aa23          	sw	a0,-2028(gp) # 80001814 <Dhrystones_Per_Second>
80000714:	00098593          	mv	a1,s3
80000718:	00001517          	auipc	a0,0x1
8000071c:	fc450513          	addi	a0,a0,-60 # 800016dc <__init_array_end+0x4c8>
80000720:	2c0000ef          	jal	ra,800009e0 <printf>
80000724:	81418793          	addi	a5,gp,-2028 # 80001814 <Dhrystones_Per_Second>
80000728:	0007a583          	lw	a1,0(a5)
8000072c:	00001517          	auipc	a0,0x1
80000730:	fe450513          	addi	a0,a0,-28 # 80001710 <__init_array_end+0x4fc>
80000734:	2ac000ef          	jal	ra,800009e0 <printf>
80000738:	00092583          	lw	a1,0(s2)
8000073c:	00001517          	auipc	a0,0x1
80000740:	00850513          	addi	a0,a0,8 # 80001744 <__init_array_end+0x530>
80000744:	29c000ef          	jal	ra,800009e0 <printf>
80000748:	000a0593          	mv	a1,s4
8000074c:	00001517          	auipc	a0,0x1
80000750:	00850513          	addi	a0,a0,8 # 80001754 <__init_array_end+0x540>
80000754:	28c000ef          	jal	ra,800009e0 <printf>
80000758:	00048593          	mv	a1,s1
8000075c:	00001517          	auipc	a0,0x1
80000760:	01050513          	addi	a0,a0,16 # 8000176c <__init_array_end+0x558>
80000764:	27c000ef          	jal	ra,800009e0 <printf>
80000768:	00001517          	auipc	a0,0x1
8000076c:	01050513          	addi	a0,a0,16 # 80001778 <__init_array_end+0x564>
80000770:	270000ef          	jal	ra,800009e0 <printf>
80000774:	0000e537          	lui	a0,0xe
80000778:	e4450513          	addi	a0,a0,-444 # de44 <__stack_size+0xce44>
8000077c:	02aa15b3          	mulh	a1,s4,a0
80000780:	00092603          	lw	a2,0(s2)
80000784:	f9c00493          	li	s1,-100
80000788:	41f65693          	srai	a3,a2,0x1f
8000078c:	02aa0533          	mul	a0,s4,a0
80000790:	398000ef          	jal	ra,80000b28 <__divdi3>
80000794:	00050793          	mv	a5,a0
80000798:	06400593          	li	a1,100
8000079c:	02b7c5b3          	div	a1,a5,a1
800007a0:	00001517          	auipc	a0,0x1
800007a4:	00850513          	addi	a0,a0,8 # 800017a8 <__init_array_end+0x594>
800007a8:	029584b3          	mul	s1,a1,s1
800007ac:	00f484b3          	add	s1,s1,a5
800007b0:	230000ef          	jal	ra,800009e0 <printf>
800007b4:	00900793          	li	a5,9
800007b8:	1097d863          	bge	a5,s1,800008c8 <main+0x844>
800007bc:	00048593          	mv	a1,s1
800007c0:	00001517          	auipc	a0,0x1
800007c4:	f9050513          	addi	a0,a0,-112 # 80001750 <__init_array_end+0x53c>
800007c8:	218000ef          	jal	ra,800009e0 <printf>
800007cc:	f6040113          	addi	sp,s0,-160
800007d0:	09c12083          	lw	ra,156(sp)
800007d4:	00000513          	li	a0,0
800007d8:	09812403          	lw	s0,152(sp)
800007dc:	09412483          	lw	s1,148(sp)
800007e0:	09012903          	lw	s2,144(sp)
800007e4:	08c12983          	lw	s3,140(sp)
800007e8:	08812a03          	lw	s4,136(sp)
800007ec:	08412a83          	lw	s5,132(sp)
800007f0:	08012b03          	lw	s6,128(sp)
800007f4:	07c12b83          	lw	s7,124(sp)
800007f8:	07812c03          	lw	s8,120(sp)
800007fc:	07412c83          	lw	s9,116(sp)
80000800:	07012d03          	lw	s10,112(sp)
80000804:	06c12d83          	lw	s11,108(sp)
80000808:	0a010113          	addi	sp,sp,160
8000080c:	00008067          	ret
80000810:	f7c40593          	addi	a1,s0,-132
80000814:	00000513          	li	a0,0
80000818:	550000ef          	jal	ra,80000d68 <Proc_6>
8000081c:	01e4c783          	lbu	a5,30(s1)
80000820:	0004a303          	lw	t1,0(s1)
80000824:	0044a883          	lw	a7,4(s1)
80000828:	0084a803          	lw	a6,8(s1)
8000082c:	00c4a503          	lw	a0,12(s1)
80000830:	0104a583          	lw	a1,16(s1)
80000834:	0144a603          	lw	a2,20(s1)
80000838:	0184a683          	lw	a3,24(s1)
8000083c:	01c4d703          	lhu	a4,28(s1)
80000840:	faf40f23          	sb	a5,-66(s0)
80000844:	001b0b13          	addi	s6,s6,1
80000848:	8341ac23          	sw	s4,-1992(gp) # 80001838 <Int_Glob>
8000084c:	fa642023          	sw	t1,-96(s0)
80000850:	fb142223          	sw	a7,-92(s0)
80000854:	fb042423          	sw	a6,-88(s0)
80000858:	faa42623          	sw	a0,-84(s0)
8000085c:	fab42823          	sw	a1,-80(s0)
80000860:	fac42a23          	sw	a2,-76(s0)
80000864:	fad42c23          	sw	a3,-72(s0)
80000868:	fae41e23          	sh	a4,-68(s0)
8000086c:	0ffb7b13          	andi	s6,s6,255
80000870:	8301c783          	lbu	a5,-2000(gp) # 80001830 <Ch_2_Glob>
80000874:	000a0a93          	mv	s5,s4
80000878:	ab67fce3          	bgeu	a5,s6,80000330 <main+0x2ac>
8000087c:	ad9ff06f          	j	80000354 <main+0x2d0>
80000880:	00001517          	auipc	a0,0x1
80000884:	a8450513          	addi	a0,a0,-1404 # 80001304 <__init_array_end+0xf0>
80000888:	25c000ef          	jal	ra,80000ae4 <puts>
8000088c:	f6c42703          	lw	a4,-148(s0)
80000890:	00a00513          	li	a0,10
80000894:	00271793          	slli	a5,a4,0x2
80000898:	00e787b3          	add	a5,a5,a4
8000089c:	00179793          	slli	a5,a5,0x1
800008a0:	f6f42623          	sw	a5,-148(s0)
800008a4:	07c000ef          	jal	ra,80000920 <putchar>
800008a8:	82818793          	addi	a5,gp,-2008 # 80001828 <Done>
800008ac:	0007a783          	lw	a5,0(a5)
800008b0:	96078ce3          	beqz	a5,80000228 <main+0x1a4>
800008b4:	b05ff06f          	j	800003b8 <main+0x334>
800008b8:	00001517          	auipc	a0,0x1
800008bc:	9dc50513          	addi	a0,a0,-1572 # 80001294 <__init_array_end+0x80>
800008c0:	224000ef          	jal	ra,80000ae4 <puts>
800008c4:	915ff06f          	j	800001d8 <main+0x154>
800008c8:	03000513          	li	a0,48
800008cc:	054000ef          	jal	ra,80000920 <putchar>
800008d0:	eedff06f          	j	800007bc <main+0x738>

800008d4 <write_u32.constprop.0>:
800008d4:	100007b7          	lui	a5,0x10000
800008d8:	00a7a023          	sw	a0,0(a5) # 10000000 <__stack_size+0xffff000>
800008dc:	00008067          	ret

800008e0 <sim_putchar>:
800008e0:	ff010113          	addi	sp,sp,-16
800008e4:	00812423          	sw	s0,8(sp)
800008e8:	00112623          	sw	ra,12(sp)
800008ec:	00050413          	mv	s0,a0
800008f0:	fe5ff0ef          	jal	ra,800008d4 <write_u32.constprop.0>
800008f4:	00040513          	mv	a0,s0
800008f8:	00c12083          	lw	ra,12(sp)
800008fc:	00812403          	lw	s0,8(sp)
80000900:	01010113          	addi	sp,sp,16
80000904:	00008067          	ret

80000908 <read_u32.constprop.1>:
80000908:	1001c7b7          	lui	a5,0x1001c
8000090c:	ff87a503          	lw	a0,-8(a5) # 1001bff8 <__stack_size+0x1001aff8>
80000910:	00008067          	ret

80000914 <sim_time>:
80000914:	ff5ff06f          	j	80000908 <read_u32.constprop.1>

80000918 <setStats>:
80000918:	00008067          	ret

8000091c <time>:
8000091c:	ff9ff06f          	j	80000914 <sim_time>

80000920 <putchar>:
80000920:	fc1ff06f          	j	800008e0 <sim_putchar>

80000924 <printf_s>:
80000924:	ff010113          	addi	sp,sp,-16
80000928:	00812423          	sw	s0,8(sp)
8000092c:	00112623          	sw	ra,12(sp)
80000930:	00050413          	mv	s0,a0
80000934:	00054503          	lbu	a0,0(a0)
80000938:	00050a63          	beqz	a0,8000094c <printf_s+0x28>
8000093c:	00140413          	addi	s0,s0,1
80000940:	fe1ff0ef          	jal	ra,80000920 <putchar>
80000944:	00044503          	lbu	a0,0(s0)
80000948:	fe051ae3          	bnez	a0,8000093c <printf_s+0x18>
8000094c:	00c12083          	lw	ra,12(sp)
80000950:	00812403          	lw	s0,8(sp)
80000954:	01010113          	addi	sp,sp,16
80000958:	00008067          	ret

8000095c <printf_c>:
8000095c:	fc5ff06f          	j	80000920 <putchar>

80000960 <printf_d>:
80000960:	fd010113          	addi	sp,sp,-48
80000964:	02912223          	sw	s1,36(sp)
80000968:	02112623          	sw	ra,44(sp)
8000096c:	02812423          	sw	s0,40(sp)
80000970:	03212023          	sw	s2,32(sp)
80000974:	00050493          	mv	s1,a0
80000978:	04054c63          	bltz	a0,800009d0 <printf_d+0x70>
8000097c:	00010913          	mv	s2,sp
80000980:	00090413          	mv	s0,s2
80000984:	00a00713          	li	a4,10
80000988:	00049463          	bnez	s1,80000990 <printf_d+0x30>
8000098c:	01241e63          	bne	s0,s2,800009a8 <printf_d+0x48>
80000990:	02e4e7b3          	rem	a5,s1,a4
80000994:	00140413          	addi	s0,s0,1
80000998:	03078793          	addi	a5,a5,48
8000099c:	02e4c4b3          	div	s1,s1,a4
800009a0:	fef40fa3          	sb	a5,-1(s0)
800009a4:	fe5ff06f          	j	80000988 <printf_d+0x28>
800009a8:	fff40413          	addi	s0,s0,-1
800009ac:	00044503          	lbu	a0,0(s0)
800009b0:	fadff0ef          	jal	ra,8000095c <printf_c>
800009b4:	ff241ae3          	bne	s0,s2,800009a8 <printf_d+0x48>
800009b8:	02c12083          	lw	ra,44(sp)
800009bc:	02812403          	lw	s0,40(sp)
800009c0:	02412483          	lw	s1,36(sp)
800009c4:	02012903          	lw	s2,32(sp)
800009c8:	03010113          	addi	sp,sp,48
800009cc:	00008067          	ret
800009d0:	02d00513          	li	a0,45
800009d4:	f89ff0ef          	jal	ra,8000095c <printf_c>
800009d8:	409004b3          	neg	s1,s1
800009dc:	fa1ff06f          	j	8000097c <printf_d+0x1c>

800009e0 <printf>:
800009e0:	fb010113          	addi	sp,sp,-80
800009e4:	02912223          	sw	s1,36(sp)
800009e8:	02112623          	sw	ra,44(sp)
800009ec:	02812423          	sw	s0,40(sp)
800009f0:	03212023          	sw	s2,32(sp)
800009f4:	01312e23          	sw	s3,28(sp)
800009f8:	01412c23          	sw	s4,24(sp)
800009fc:	01512a23          	sw	s5,20(sp)
80000a00:	00050493          	mv	s1,a0
80000a04:	00054503          	lbu	a0,0(a0)
80000a08:	04f12223          	sw	a5,68(sp)
80000a0c:	03410793          	addi	a5,sp,52
80000a10:	02b12a23          	sw	a1,52(sp)
80000a14:	02c12c23          	sw	a2,56(sp)
80000a18:	02d12e23          	sw	a3,60(sp)
80000a1c:	04e12023          	sw	a4,64(sp)
80000a20:	05012423          	sw	a6,72(sp)
80000a24:	05112623          	sw	a7,76(sp)
80000a28:	00f12623          	sw	a5,12(sp)
80000a2c:	02050863          	beqz	a0,80000a5c <printf+0x7c>
80000a30:	00000413          	li	s0,0
80000a34:	02500a93          	li	s5,37
80000a38:	06300913          	li	s2,99
80000a3c:	07300993          	li	s3,115
80000a40:	06400a13          	li	s4,100
80000a44:	05550263          	beq	a0,s5,80000a88 <printf+0xa8>
80000a48:	f15ff0ef          	jal	ra,8000095c <printf_c>
80000a4c:	00140413          	addi	s0,s0,1
80000a50:	008487b3          	add	a5,s1,s0
80000a54:	0007c503          	lbu	a0,0(a5)
80000a58:	fe0516e3          	bnez	a0,80000a44 <printf+0x64>
80000a5c:	02c12083          	lw	ra,44(sp)
80000a60:	02812403          	lw	s0,40(sp)
80000a64:	02412483          	lw	s1,36(sp)
80000a68:	02012903          	lw	s2,32(sp)
80000a6c:	01c12983          	lw	s3,28(sp)
80000a70:	01812a03          	lw	s4,24(sp)
80000a74:	01412a83          	lw	s5,20(sp)
80000a78:	05010113          	addi	sp,sp,80
80000a7c:	00008067          	ret
80000a80:	03378a63          	beq	a5,s3,80000ab4 <printf+0xd4>
80000a84:	05478463          	beq	a5,s4,80000acc <printf+0xec>
80000a88:	00140413          	addi	s0,s0,1
80000a8c:	008487b3          	add	a5,s1,s0
80000a90:	0007c783          	lbu	a5,0(a5)
80000a94:	fa078ce3          	beqz	a5,80000a4c <printf+0x6c>
80000a98:	ff2794e3          	bne	a5,s2,80000a80 <printf+0xa0>
80000a9c:	00c12783          	lw	a5,12(sp)
80000aa0:	0007a503          	lw	a0,0(a5)
80000aa4:	00478793          	addi	a5,a5,4
80000aa8:	00f12623          	sw	a5,12(sp)
80000aac:	eb1ff0ef          	jal	ra,8000095c <printf_c>
80000ab0:	f9dff06f          	j	80000a4c <printf+0x6c>
80000ab4:	00c12783          	lw	a5,12(sp)
80000ab8:	0007a503          	lw	a0,0(a5)
80000abc:	00478793          	addi	a5,a5,4
80000ac0:	00f12623          	sw	a5,12(sp)
80000ac4:	e61ff0ef          	jal	ra,80000924 <printf_s>
80000ac8:	f85ff06f          	j	80000a4c <printf+0x6c>
80000acc:	00c12783          	lw	a5,12(sp)
80000ad0:	0007a503          	lw	a0,0(a5)
80000ad4:	00478793          	addi	a5,a5,4
80000ad8:	00f12623          	sw	a5,12(sp)
80000adc:	e85ff0ef          	jal	ra,80000960 <printf_d>
80000ae0:	f6dff06f          	j	80000a4c <printf+0x6c>

80000ae4 <puts>:
80000ae4:	ff010113          	addi	sp,sp,-16
80000ae8:	00812423          	sw	s0,8(sp)
80000aec:	00112623          	sw	ra,12(sp)
80000af0:	00050413          	mv	s0,a0
80000af4:	00054503          	lbu	a0,0(a0)
80000af8:	00050a63          	beqz	a0,80000b0c <puts+0x28>
80000afc:	00140413          	addi	s0,s0,1
80000b00:	e21ff0ef          	jal	ra,80000920 <putchar>
80000b04:	00044503          	lbu	a0,0(s0)
80000b08:	fe051ae3          	bnez	a0,80000afc <puts+0x18>
80000b0c:	00a00513          	li	a0,10
80000b10:	e11ff0ef          	jal	ra,80000920 <putchar>
80000b14:	00c12083          	lw	ra,12(sp)
80000b18:	00812403          	lw	s0,8(sp)
80000b1c:	00000513          	li	a0,0
80000b20:	01010113          	addi	sp,sp,16
80000b24:	00008067          	ret

80000b28 <__divdi3>:
80000b28:	fd010113          	addi	sp,sp,-48
80000b2c:	01412e23          	sw	s4,28(sp)
80000b30:	01512c23          	sw	s5,24(sp)
80000b34:	02812623          	sw	s0,44(sp)
80000b38:	02912423          	sw	s1,40(sp)
80000b3c:	03212223          	sw	s2,36(sp)
80000b40:	03312023          	sw	s3,32(sp)
80000b44:	01612a23          	sw	s6,20(sp)
80000b48:	01712823          	sw	s7,16(sp)
80000b4c:	01812623          	sw	s8,12(sp)
80000b50:	00050a13          	mv	s4,a0
80000b54:	00058a93          	mv	s5,a1
80000b58:	00000793          	li	a5,0
80000b5c:	00000713          	li	a4,0
80000b60:	0206c463          	bltz	a3,80000b88 <__divdi3+0x60>
80000b64:	01f65313          	srli	t1,a2,0x1f
80000b68:	00178813          	addi	a6,a5,1
80000b6c:	00169693          	slli	a3,a3,0x1
80000b70:	00f838b3          	sltu	a7,a6,a5
80000b74:	00d366b3          	or	a3,t1,a3
80000b78:	00161613          	slli	a2,a2,0x1
80000b7c:	00080793          	mv	a5,a6
80000b80:	00e88733          	add	a4,a7,a4
80000b84:	fe06d0e3          	bgez	a3,80000b64 <__divdi3+0x3c>
80000b88:	015a6833          	or	a6,s4,s5
80000b8c:	00000513          	li	a0,0
80000b90:	00000593          	li	a1,0
80000b94:	00100c13          	li	s8,1
80000b98:	0c080263          	beqz	a6,80000c5c <__divdi3+0x134>
80000b9c:	fff78813          	addi	a6,a5,-1
80000ba0:	00e7e8b3          	or	a7,a5,a4
80000ba4:	01f69e93          	slli	t4,a3,0x1f
80000ba8:	00f83f33          	sltu	t5,a6,a5
80000bac:	00165f93          	srli	t6,a2,0x1
80000bb0:	fff70293          	addi	t0,a4,-1
80000bb4:	08dae863          	bltu	s5,a3,80000c44 <__divdi3+0x11c>
80000bb8:	00fc1333          	sll	t1,s8,a5
80000bbc:	40ca0e33          	sub	t3,s4,a2
80000bc0:	00080393          	mv	t2,a6
80000bc4:	01ca3933          	sltu	s2,s4,t3
80000bc8:	40da89b3          	sub	s3,s5,a3
80000bcc:	00088413          	mv	s0,a7
80000bd0:	41f35493          	srai	s1,t1,0x1f
80000bd4:	000e8b93          	mv	s7,t4
80000bd8:	000f0b13          	mv	s6,t5
80000bdc:	000f8793          	mv	a5,t6
80000be0:	00028713          	mv	a4,t0
80000be4:	01569463          	bne	a3,s5,80000bec <__divdi3+0xc4>
80000be8:	04ca6e63          	bltu	s4,a2,80000c44 <__divdi3+0x11c>
80000bec:	41298ab3          	sub	s5,s3,s2
80000bf0:	000e0a13          	mv	s4,t3
80000bf4:	015e6833          	or	a6,t3,s5
80000bf8:	00656533          	or	a0,a0,t1
80000bfc:	0095e5b3          	or	a1,a1,s1
80000c00:	00040c63          	beqz	s0,80000c18 <__divdi3+0xf0>
80000c04:	00fbe633          	or	a2,s7,a5
80000c08:	0016d693          	srli	a3,a3,0x1
80000c0c:	00038793          	mv	a5,t2
80000c10:	00eb0733          	add	a4,s6,a4
80000c14:	f80814e3          	bnez	a6,80000b9c <__divdi3+0x74>
80000c18:	02c12403          	lw	s0,44(sp)
80000c1c:	02812483          	lw	s1,40(sp)
80000c20:	02412903          	lw	s2,36(sp)
80000c24:	02012983          	lw	s3,32(sp)
80000c28:	01c12a03          	lw	s4,28(sp)
80000c2c:	01812a83          	lw	s5,24(sp)
80000c30:	01412b03          	lw	s6,20(sp)
80000c34:	01012b83          	lw	s7,16(sp)
80000c38:	00c12c03          	lw	s8,12(sp)
80000c3c:	03010113          	addi	sp,sp,48
80000c40:	00008067          	ret
80000c44:	fc088ae3          	beqz	a7,80000c18 <__divdi3+0xf0>
80000c48:	01fee633          	or	a2,t4,t6
80000c4c:	0016d693          	srli	a3,a3,0x1
80000c50:	00080793          	mv	a5,a6
80000c54:	005f0733          	add	a4,t5,t0
80000c58:	f45ff06f          	j	80000b9c <__divdi3+0x74>
80000c5c:	000a0513          	mv	a0,s4
80000c60:	000a8593          	mv	a1,s5
80000c64:	fb5ff06f          	j	80000c18 <__divdi3+0xf0>

80000c68 <Proc_7>:
80000c68:	00250513          	addi	a0,a0,2
80000c6c:	00b505b3          	add	a1,a0,a1
80000c70:	00b62023          	sw	a1,0(a2)
80000c74:	00008067          	ret

80000c78 <Proc_8>:
80000c78:	00560713          	addi	a4,a2,5
80000c7c:	0c800813          	li	a6,200
80000c80:	03070833          	mul	a6,a4,a6
80000c84:	00261613          	slli	a2,a2,0x2
80000c88:	00271793          	slli	a5,a4,0x2
80000c8c:	00f50533          	add	a0,a0,a5
80000c90:	00d52023          	sw	a3,0(a0)
80000c94:	06e52c23          	sw	a4,120(a0)
80000c98:	00d52223          	sw	a3,4(a0)
80000c9c:	00c807b3          	add	a5,a6,a2
80000ca0:	00f587b3          	add	a5,a1,a5
80000ca4:	0107a683          	lw	a3,16(a5)
80000ca8:	00e7aa23          	sw	a4,20(a5)
80000cac:	00e7ac23          	sw	a4,24(a5)
80000cb0:	00168713          	addi	a4,a3,1
80000cb4:	00e7a823          	sw	a4,16(a5)
80000cb8:	00052783          	lw	a5,0(a0)
80000cbc:	010585b3          	add	a1,a1,a6
80000cc0:	00c585b3          	add	a1,a1,a2
80000cc4:	00001637          	lui	a2,0x1
80000cc8:	00b605b3          	add	a1,a2,a1
80000ccc:	faf5aa23          	sw	a5,-76(a1)
80000cd0:	00500793          	li	a5,5
80000cd4:	82f1ac23          	sw	a5,-1992(gp) # 80001838 <Int_Glob>
80000cd8:	00008067          	ret

80000cdc <Func_1>:
80000cdc:	0ff57513          	andi	a0,a0,255
80000ce0:	0ff5f593          	andi	a1,a1,255
80000ce4:	00b50663          	beq	a0,a1,80000cf0 <Func_1+0x14>
80000ce8:	00000513          	li	a0,0
80000cec:	00008067          	ret
80000cf0:	82a188a3          	sb	a0,-1999(gp) # 80001831 <Ch_1_Glob>
80000cf4:	00100513          	li	a0,1
80000cf8:	00008067          	ret

80000cfc <Func_2>:
80000cfc:	ff010113          	addi	sp,sp,-16
80000d00:	00812423          	sw	s0,8(sp)
80000d04:	00912223          	sw	s1,4(sp)
80000d08:	00112623          	sw	ra,12(sp)
80000d0c:	00050413          	mv	s0,a0
80000d10:	00058493          	mv	s1,a1
80000d14:	0034c583          	lbu	a1,3(s1)
80000d18:	00244503          	lbu	a0,2(s0)
80000d1c:	fc1ff0ef          	jal	ra,80000cdc <Func_1>
80000d20:	fe051ae3          	bnez	a0,80000d14 <Func_2+0x18>
80000d24:	00048593          	mv	a1,s1
80000d28:	00040513          	mv	a0,s0
80000d2c:	36c000ef          	jal	ra,80001098 <strcmp>
80000d30:	00000793          	li	a5,0
80000d34:	00a05863          	blez	a0,80000d44 <Func_2+0x48>
80000d38:	00a00793          	li	a5,10
80000d3c:	82f1ac23          	sw	a5,-1992(gp) # 80001838 <Int_Glob>
80000d40:	00100793          	li	a5,1
80000d44:	00c12083          	lw	ra,12(sp)
80000d48:	00812403          	lw	s0,8(sp)
80000d4c:	00412483          	lw	s1,4(sp)
80000d50:	00078513          	mv	a0,a5
80000d54:	01010113          	addi	sp,sp,16
80000d58:	00008067          	ret

80000d5c <Func_3>:
80000d5c:	ffe50513          	addi	a0,a0,-2
80000d60:	00153513          	seqz	a0,a0
80000d64:	00008067          	ret

80000d68 <Proc_6>:
80000d68:	ff010113          	addi	sp,sp,-16
80000d6c:	00812423          	sw	s0,8(sp)
80000d70:	00912223          	sw	s1,4(sp)
80000d74:	00050413          	mv	s0,a0
80000d78:	00112623          	sw	ra,12(sp)
80000d7c:	00058493          	mv	s1,a1
80000d80:	fddff0ef          	jal	ra,80000d5c <Func_3>
80000d84:	00040793          	mv	a5,s0
80000d88:	00051463          	bnez	a0,80000d90 <Proc_6+0x28>
80000d8c:	00300793          	li	a5,3
80000d90:	00f4a023          	sw	a5,0(s1)
80000d94:	00100713          	li	a4,1
80000d98:	02e40863          	beq	s0,a4,80000dc8 <Proc_6+0x60>
80000d9c:	02040e63          	beqz	s0,80000dd8 <Proc_6+0x70>
80000da0:	00200793          	li	a5,2
80000da4:	04f40663          	beq	s0,a5,80000df0 <Proc_6+0x88>
80000da8:	00400713          	li	a4,4
80000dac:	00e41463          	bne	s0,a4,80000db4 <Proc_6+0x4c>
80000db0:	00f4a023          	sw	a5,0(s1)
80000db4:	00c12083          	lw	ra,12(sp)
80000db8:	00812403          	lw	s0,8(sp)
80000dbc:	00412483          	lw	s1,4(sp)
80000dc0:	01010113          	addi	sp,sp,16
80000dc4:	00008067          	ret
80000dc8:	83818793          	addi	a5,gp,-1992 # 80001838 <Int_Glob>
80000dcc:	0007a703          	lw	a4,0(a5)
80000dd0:	06400793          	li	a5,100
80000dd4:	02e7da63          	bge	a5,a4,80000e08 <Proc_6+0xa0>
80000dd8:	00c12083          	lw	ra,12(sp)
80000ddc:	00812403          	lw	s0,8(sp)
80000de0:	0004a023          	sw	zero,0(s1)
80000de4:	00412483          	lw	s1,4(sp)
80000de8:	01010113          	addi	sp,sp,16
80000dec:	00008067          	ret
80000df0:	00c12083          	lw	ra,12(sp)
80000df4:	00812403          	lw	s0,8(sp)
80000df8:	00e4a023          	sw	a4,0(s1)
80000dfc:	00412483          	lw	s1,4(sp)
80000e00:	01010113          	addi	sp,sp,16
80000e04:	00008067          	ret
80000e08:	00300793          	li	a5,3
80000e0c:	00f4a023          	sw	a5,0(s1)
80000e10:	fa5ff06f          	j	80000db4 <Proc_6+0x4c>

80000e14 <Proc_2>:
80000e14:	8311c703          	lbu	a4,-1999(gp) # 80001831 <Ch_1_Glob>
80000e18:	04100793          	li	a5,65
80000e1c:	00f70463          	beq	a4,a5,80000e24 <Proc_2+0x10>
80000e20:	00008067          	ret
80000e24:	00052783          	lw	a5,0(a0)
80000e28:	83818713          	addi	a4,gp,-1992 # 80001838 <Int_Glob>
80000e2c:	00072703          	lw	a4,0(a4)
80000e30:	00978793          	addi	a5,a5,9
80000e34:	40e787b3          	sub	a5,a5,a4
80000e38:	00f52023          	sw	a5,0(a0)
80000e3c:	00008067          	ret

80000e40 <Proc_3>:
80000e40:	84018793          	addi	a5,gp,-1984 # 80001840 <Ptr_Glob>
80000e44:	0007a603          	lw	a2,0(a5)
80000e48:	00060863          	beqz	a2,80000e58 <Proc_3+0x18>
80000e4c:	00062703          	lw	a4,0(a2) # 1000 <__stack_size>
80000e50:	00e52023          	sw	a4,0(a0)
80000e54:	0007a603          	lw	a2,0(a5)
80000e58:	83818793          	addi	a5,gp,-1992 # 80001838 <Int_Glob>
80000e5c:	0007a583          	lw	a1,0(a5)
80000e60:	00c60613          	addi	a2,a2,12
80000e64:	00a00513          	li	a0,10
80000e68:	e01ff06f          	j	80000c68 <Proc_7>

80000e6c <Proc_1>:
80000e6c:	ff010113          	addi	sp,sp,-16
80000e70:	01212023          	sw	s2,0(sp)
80000e74:	84018913          	addi	s2,gp,-1984 # 80001840 <Ptr_Glob>
80000e78:	00092783          	lw	a5,0(s2)
80000e7c:	00812423          	sw	s0,8(sp)
80000e80:	00052403          	lw	s0,0(a0)
80000e84:	0007a703          	lw	a4,0(a5)
80000e88:	00912223          	sw	s1,4(sp)
80000e8c:	0047ae83          	lw	t4,4(a5)
80000e90:	0087ae03          	lw	t3,8(a5)
80000e94:	0107a303          	lw	t1,16(a5)
80000e98:	0147a883          	lw	a7,20(a5)
80000e9c:	0187a803          	lw	a6,24(a5)
80000ea0:	0207a583          	lw	a1,32(a5)
80000ea4:	0247a603          	lw	a2,36(a5)
80000ea8:	0287a683          	lw	a3,40(a5)
80000eac:	00112623          	sw	ra,12(sp)
80000eb0:	00050493          	mv	s1,a0
80000eb4:	01c7a503          	lw	a0,28(a5)
80000eb8:	02c7a783          	lw	a5,44(a5)
80000ebc:	00e42023          	sw	a4,0(s0)
80000ec0:	0004a703          	lw	a4,0(s1)
80000ec4:	00a42e23          	sw	a0,28(s0)
80000ec8:	02f42623          	sw	a5,44(s0)
80000ecc:	01d42223          	sw	t4,4(s0)
80000ed0:	00500793          	li	a5,5
80000ed4:	01c42423          	sw	t3,8(s0)
80000ed8:	00642823          	sw	t1,16(s0)
80000edc:	01142a23          	sw	a7,20(s0)
80000ee0:	01042c23          	sw	a6,24(s0)
80000ee4:	02b42023          	sw	a1,32(s0)
80000ee8:	02c42223          	sw	a2,36(s0)
80000eec:	02d42423          	sw	a3,40(s0)
80000ef0:	00f4a623          	sw	a5,12(s1)
80000ef4:	00f42623          	sw	a5,12(s0)
80000ef8:	00e42023          	sw	a4,0(s0)
80000efc:	00040513          	mv	a0,s0
80000f00:	f41ff0ef          	jal	ra,80000e40 <Proc_3>
80000f04:	00442783          	lw	a5,4(s0)
80000f08:	08078063          	beqz	a5,80000f88 <Proc_1+0x11c>
80000f0c:	0004a783          	lw	a5,0(s1)
80000f10:	00c12083          	lw	ra,12(sp)
80000f14:	00812403          	lw	s0,8(sp)
80000f18:	0007af83          	lw	t6,0(a5)
80000f1c:	0047af03          	lw	t5,4(a5)
80000f20:	0087ae83          	lw	t4,8(a5)
80000f24:	00c7ae03          	lw	t3,12(a5)
80000f28:	0107a303          	lw	t1,16(a5)
80000f2c:	0147a883          	lw	a7,20(a5)
80000f30:	0187a803          	lw	a6,24(a5)
80000f34:	01c7a583          	lw	a1,28(a5)
80000f38:	0207a603          	lw	a2,32(a5)
80000f3c:	0247a683          	lw	a3,36(a5)
80000f40:	0287a703          	lw	a4,40(a5)
80000f44:	02c7a783          	lw	a5,44(a5)
80000f48:	01f4a023          	sw	t6,0(s1)
80000f4c:	01e4a223          	sw	t5,4(s1)
80000f50:	01d4a423          	sw	t4,8(s1)
80000f54:	01c4a623          	sw	t3,12(s1)
80000f58:	0064a823          	sw	t1,16(s1)
80000f5c:	0114aa23          	sw	a7,20(s1)
80000f60:	0104ac23          	sw	a6,24(s1)
80000f64:	00b4ae23          	sw	a1,28(s1)
80000f68:	02c4a023          	sw	a2,32(s1)
80000f6c:	02d4a223          	sw	a3,36(s1)
80000f70:	02e4a423          	sw	a4,40(s1)
80000f74:	02f4a623          	sw	a5,44(s1)
80000f78:	00012903          	lw	s2,0(sp)
80000f7c:	00412483          	lw	s1,4(sp)
80000f80:	01010113          	addi	sp,sp,16
80000f84:	00008067          	ret
80000f88:	0084a503          	lw	a0,8(s1)
80000f8c:	00600793          	li	a5,6
80000f90:	00840593          	addi	a1,s0,8
80000f94:	00f42623          	sw	a5,12(s0)
80000f98:	dd1ff0ef          	jal	ra,80000d68 <Proc_6>
80000f9c:	00092783          	lw	a5,0(s2)
80000fa0:	00c42503          	lw	a0,12(s0)
80000fa4:	00c40613          	addi	a2,s0,12
80000fa8:	0007a783          	lw	a5,0(a5)
80000fac:	00c12083          	lw	ra,12(sp)
80000fb0:	00412483          	lw	s1,4(sp)
80000fb4:	00f42023          	sw	a5,0(s0)
80000fb8:	00812403          	lw	s0,8(sp)
80000fbc:	00012903          	lw	s2,0(sp)
80000fc0:	00a00593          	li	a1,10
80000fc4:	01010113          	addi	sp,sp,16
80000fc8:	ca1ff06f          	j	80000c68 <Proc_7>

80000fcc <Proc_4>:
80000fcc:	83418713          	addi	a4,gp,-1996 # 80001834 <Bool_Glob>
80000fd0:	00072683          	lw	a3,0(a4)
80000fd4:	8311c783          	lbu	a5,-1999(gp) # 80001831 <Ch_1_Glob>
80000fd8:	fbf78793          	addi	a5,a5,-65
80000fdc:	0017b793          	seqz	a5,a5
80000fe0:	00d7e7b3          	or	a5,a5,a3
80000fe4:	00f72023          	sw	a5,0(a4)
80000fe8:	04200793          	li	a5,66
80000fec:	82f18823          	sb	a5,-2000(gp) # 80001830 <Ch_2_Glob>
80000ff0:	00008067          	ret

80000ff4 <Proc_5>:
80000ff4:	04100793          	li	a5,65
80000ff8:	82f188a3          	sb	a5,-1999(gp) # 80001831 <Ch_1_Glob>
80000ffc:	8201aa23          	sw	zero,-1996(gp) # 80001834 <Bool_Glob>
80001000:	00008067          	ret

80001004 <__libc_init_array>:
80001004:	ff010113          	addi	sp,sp,-16
80001008:	00812423          	sw	s0,8(sp)
8000100c:	01212023          	sw	s2,0(sp)
80001010:	00000417          	auipc	s0,0x0
80001014:	20440413          	addi	s0,s0,516 # 80001214 <__init_array_end>
80001018:	00000917          	auipc	s2,0x0
8000101c:	1fc90913          	addi	s2,s2,508 # 80001214 <__init_array_end>
80001020:	40890933          	sub	s2,s2,s0
80001024:	00112623          	sw	ra,12(sp)
80001028:	00912223          	sw	s1,4(sp)
8000102c:	40295913          	srai	s2,s2,0x2
80001030:	00090e63          	beqz	s2,8000104c <__libc_init_array+0x48>
80001034:	00000493          	li	s1,0
80001038:	00042783          	lw	a5,0(s0)
8000103c:	00148493          	addi	s1,s1,1
80001040:	00440413          	addi	s0,s0,4
80001044:	000780e7          	jalr	a5
80001048:	fe9918e3          	bne	s2,s1,80001038 <__libc_init_array+0x34>
8000104c:	00000417          	auipc	s0,0x0
80001050:	1c840413          	addi	s0,s0,456 # 80001214 <__init_array_end>
80001054:	00000917          	auipc	s2,0x0
80001058:	1c090913          	addi	s2,s2,448 # 80001214 <__init_array_end>
8000105c:	40890933          	sub	s2,s2,s0
80001060:	40295913          	srai	s2,s2,0x2
80001064:	00090e63          	beqz	s2,80001080 <__libc_init_array+0x7c>
80001068:	00000493          	li	s1,0
8000106c:	00042783          	lw	a5,0(s0)
80001070:	00148493          	addi	s1,s1,1
80001074:	00440413          	addi	s0,s0,4
80001078:	000780e7          	jalr	a5
8000107c:	fe9918e3          	bne	s2,s1,8000106c <__libc_init_array+0x68>
80001080:	00c12083          	lw	ra,12(sp)
80001084:	00812403          	lw	s0,8(sp)
80001088:	00412483          	lw	s1,4(sp)
8000108c:	00012903          	lw	s2,0(sp)
80001090:	01010113          	addi	sp,sp,16
80001094:	00008067          	ret

80001098 <strcmp>:
80001098:	00b56733          	or	a4,a0,a1
8000109c:	fff00393          	li	t2,-1
800010a0:	00377713          	andi	a4,a4,3
800010a4:	10071063          	bnez	a4,800011a4 <strcmp+0x10c>
800010a8:	7f7f87b7          	lui	a5,0x7f7f8
800010ac:	f7f78793          	addi	a5,a5,-129 # 7f7f7f7f <__stack_size+0x7f7f6f7f>
800010b0:	00052603          	lw	a2,0(a0)
800010b4:	0005a683          	lw	a3,0(a1)
800010b8:	00f672b3          	and	t0,a2,a5
800010bc:	00f66333          	or	t1,a2,a5
800010c0:	00f282b3          	add	t0,t0,a5
800010c4:	0062e2b3          	or	t0,t0,t1
800010c8:	10729263          	bne	t0,t2,800011cc <strcmp+0x134>
800010cc:	08d61663          	bne	a2,a3,80001158 <strcmp+0xc0>
800010d0:	00452603          	lw	a2,4(a0)
800010d4:	0045a683          	lw	a3,4(a1)
800010d8:	00f672b3          	and	t0,a2,a5
800010dc:	00f66333          	or	t1,a2,a5
800010e0:	00f282b3          	add	t0,t0,a5
800010e4:	0062e2b3          	or	t0,t0,t1
800010e8:	0c729e63          	bne	t0,t2,800011c4 <strcmp+0x12c>
800010ec:	06d61663          	bne	a2,a3,80001158 <strcmp+0xc0>
800010f0:	00852603          	lw	a2,8(a0)
800010f4:	0085a683          	lw	a3,8(a1)
800010f8:	00f672b3          	and	t0,a2,a5
800010fc:	00f66333          	or	t1,a2,a5
80001100:	00f282b3          	add	t0,t0,a5
80001104:	0062e2b3          	or	t0,t0,t1
80001108:	0c729863          	bne	t0,t2,800011d8 <strcmp+0x140>
8000110c:	04d61663          	bne	a2,a3,80001158 <strcmp+0xc0>
80001110:	00c52603          	lw	a2,12(a0)
80001114:	00c5a683          	lw	a3,12(a1)
80001118:	00f672b3          	and	t0,a2,a5
8000111c:	00f66333          	or	t1,a2,a5
80001120:	00f282b3          	add	t0,t0,a5
80001124:	0062e2b3          	or	t0,t0,t1
80001128:	0c729263          	bne	t0,t2,800011ec <strcmp+0x154>
8000112c:	02d61663          	bne	a2,a3,80001158 <strcmp+0xc0>
80001130:	01052603          	lw	a2,16(a0)
80001134:	0105a683          	lw	a3,16(a1)
80001138:	00f672b3          	and	t0,a2,a5
8000113c:	00f66333          	or	t1,a2,a5
80001140:	00f282b3          	add	t0,t0,a5
80001144:	0062e2b3          	or	t0,t0,t1
80001148:	0a729c63          	bne	t0,t2,80001200 <strcmp+0x168>
8000114c:	01450513          	addi	a0,a0,20
80001150:	01458593          	addi	a1,a1,20
80001154:	f4d60ee3          	beq	a2,a3,800010b0 <strcmp+0x18>
80001158:	01061713          	slli	a4,a2,0x10
8000115c:	01069793          	slli	a5,a3,0x10
80001160:	00f71e63          	bne	a4,a5,8000117c <strcmp+0xe4>
80001164:	01065713          	srli	a4,a2,0x10
80001168:	0106d793          	srli	a5,a3,0x10
8000116c:	40f70533          	sub	a0,a4,a5
80001170:	0ff57593          	andi	a1,a0,255
80001174:	02059063          	bnez	a1,80001194 <strcmp+0xfc>
80001178:	00008067          	ret
8000117c:	01075713          	srli	a4,a4,0x10
80001180:	0107d793          	srli	a5,a5,0x10
80001184:	40f70533          	sub	a0,a4,a5
80001188:	0ff57593          	andi	a1,a0,255
8000118c:	00059463          	bnez	a1,80001194 <strcmp+0xfc>
80001190:	00008067          	ret
80001194:	0ff77713          	andi	a4,a4,255
80001198:	0ff7f793          	andi	a5,a5,255
8000119c:	40f70533          	sub	a0,a4,a5
800011a0:	00008067          	ret
800011a4:	00054603          	lbu	a2,0(a0)
800011a8:	0005c683          	lbu	a3,0(a1)
800011ac:	00150513          	addi	a0,a0,1
800011b0:	00158593          	addi	a1,a1,1
800011b4:	00d61463          	bne	a2,a3,800011bc <strcmp+0x124>
800011b8:	fe0616e3          	bnez	a2,800011a4 <strcmp+0x10c>
800011bc:	40d60533          	sub	a0,a2,a3
800011c0:	00008067          	ret
800011c4:	00450513          	addi	a0,a0,4
800011c8:	00458593          	addi	a1,a1,4
800011cc:	fcd61ce3          	bne	a2,a3,800011a4 <strcmp+0x10c>
800011d0:	00000513          	li	a0,0
800011d4:	00008067          	ret
800011d8:	00850513          	addi	a0,a0,8
800011dc:	00858593          	addi	a1,a1,8
800011e0:	fcd612e3          	bne	a2,a3,800011a4 <strcmp+0x10c>
800011e4:	00000513          	li	a0,0
800011e8:	00008067          	ret
800011ec:	00c50513          	addi	a0,a0,12
800011f0:	00c58593          	addi	a1,a1,12
800011f4:	fad618e3          	bne	a2,a3,800011a4 <strcmp+0x10c>
800011f8:	00000513          	li	a0,0
800011fc:	00008067          	ret
80001200:	01050513          	addi	a0,a0,16
80001204:	01058593          	addi	a1,a1,16
80001208:	f8d61ee3          	bne	a2,a3,800011a4 <strcmp+0x10c>
8000120c:	00000513          	li	a0,0
80001210:	00008067          	ret

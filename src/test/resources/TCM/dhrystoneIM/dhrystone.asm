
build/dhrystone.elf:     file format elf32-littleriscv


Disassembly of section .init:

40000000 <_start>:
40000000:	00002197          	auipc	gp,0x2
40000004:	00018193          	mv	gp,gp

40000008 <init>:
40000008:	00005117          	auipc	sp,0x5
4000000c:	01810113          	addi	sp,sp,24 # 40005020 <__freertos_irq_stack_top>
40000010:	00001517          	auipc	a0,0x1
40000014:	20450513          	addi	a0,a0,516 # 40001214 <__init_array_end>
40000018:	00001597          	auipc	a1,0x1
4000001c:	1fc58593          	addi	a1,a1,508 # 40001214 <__init_array_end>
40000020:	81418613          	addi	a2,gp,-2028 # 40001814 <Dhrystones_Per_Second>
40000024:	00c5fc63          	bgeu	a1,a2,4000003c <init+0x34>
40000028:	00052283          	lw	t0,0(a0)
4000002c:	0055a023          	sw	t0,0(a1)
40000030:	00450513          	addi	a0,a0,4
40000034:	00458593          	addi	a1,a1,4
40000038:	fec5e8e3          	bltu	a1,a2,40000028 <init+0x20>
4000003c:	81418513          	addi	a0,gp,-2028 # 40001814 <Dhrystones_Per_Second>
40000040:	00004597          	auipc	a1,0x4
40000044:	fe058593          	addi	a1,a1,-32 # 40004020 <_end>
40000048:	00b57863          	bgeu	a0,a1,40000058 <init+0x50>
4000004c:	00052023          	sw	zero,0(a0)
40000050:	00450513          	addi	a0,a0,4
40000054:	feb56ce3          	bltu	a0,a1,4000004c <init+0x44>
40000058:	7ad000ef          	jal	ra,40001004 <__libc_init_array>
4000005c:	000060b7          	lui	ra,0x6
40000060:	3000b073          	csrc	mstatus,ra
40000064:	000020b7          	lui	ra,0x2
40000068:	3000a073          	csrs	mstatus,ra
4000006c:	018000ef          	jal	ra,40000084 <main>

40000070 <pass>:
40000070:	0000006f          	j	40000070 <pass>
40000074:	00000013          	nop

40000078 <fail>:
40000078:	0000006f          	j	40000078 <fail>
4000007c:	00000013          	nop

40000080 <_init>:
40000080:	00008067          	ret

Disassembly of section .text:

40000084 <main>:
40000084:	f6010113          	addi	sp,sp,-160
40000088:	08112e23          	sw	ra,156(sp)
4000008c:	08812c23          	sw	s0,152(sp)
40000090:	08912a23          	sw	s1,148(sp)
40000094:	0a010413          	addi	s0,sp,160
40000098:	09212823          	sw	s2,144(sp)
4000009c:	09312623          	sw	s3,140(sp)
400000a0:	09412423          	sw	s4,136(sp)
400000a4:	09512223          	sw	s5,132(sp)
400000a8:	09612023          	sw	s6,128(sp)
400000ac:	07712e23          	sw	s7,124(sp)
400000b0:	07812c23          	sw	s8,120(sp)
400000b4:	07912a23          	sw	s9,116(sp)
400000b8:	07a12823          	sw	s10,112(sp)
400000bc:	07b12623          	sw	s11,108(sp)
400000c0:	fc010113          	addi	sp,sp,-64
400000c4:	00001717          	auipc	a4,0x1
400000c8:	15070713          	addi	a4,a4,336 # 40001214 <__init_array_end>
400000cc:	00f10613          	addi	a2,sp,15
400000d0:	fc010113          	addi	sp,sp,-64
400000d4:	00001797          	auipc	a5,0x1
400000d8:	6d878793          	addi	a5,a5,1752 # 400017ac <__init_array_end+0x598>
400000dc:	00872f83          	lw	t6,8(a4)
400000e0:	00c72f03          	lw	t5,12(a4)
400000e4:	01072e83          	lw	t4,16(a4)
400000e8:	01472e03          	lw	t3,20(a4)
400000ec:	01872303          	lw	t1,24(a4)
400000f0:	00072683          	lw	a3,0(a4)
400000f4:	00472283          	lw	t0,4(a4)
400000f8:	01c75883          	lhu	a7,28(a4)
400000fc:	01e74803          	lbu	a6,30(a4)
40000100:	00f10713          	addi	a4,sp,15
40000104:	ff077713          	andi	a4,a4,-16
40000108:	0007a583          	lw	a1,0(a5)
4000010c:	00200393          	li	t2,2
40000110:	ff067613          	andi	a2,a2,-16
40000114:	00772423          	sw	t2,8(a4)
40000118:	02800393          	li	t2,40
4000011c:	00772623          	sw	t2,12(a4)
40000120:	00c72023          	sw	a2,0(a4)
40000124:	00d72823          	sw	a3,16(a4)
40000128:	82c1ae23          	sw	a2,-1988(gp) # 4000183c <Next_Ptr_Glob>
4000012c:	00072223          	sw	zero,4(a4)
40000130:	84e1a023          	sw	a4,-1984(gp) # 40001840 <Ptr_Glob>
40000134:	0047a503          	lw	a0,4(a5)
40000138:	00572a23          	sw	t0,20(a4)
4000013c:	0147a603          	lw	a2,20(a5)
40000140:	0187a683          	lw	a3,24(a5)
40000144:	01f72c23          	sw	t6,24(a4)
40000148:	01e72e23          	sw	t5,28(a4)
4000014c:	03d72023          	sw	t4,32(a4)
40000150:	03c72223          	sw	t3,36(a4)
40000154:	02672423          	sw	t1,40(a4)
40000158:	03171623          	sh	a7,44(a4)
4000015c:	03070723          	sb	a6,46(a4)
40000160:	0087a883          	lw	a7,8(a5)
40000164:	01c7d703          	lhu	a4,28(a5)
40000168:	00c7a803          	lw	a6,12(a5)
4000016c:	f8b42023          	sw	a1,-128(s0)
40000170:	0107a583          	lw	a1,16(a5)
40000174:	01e7c783          	lbu	a5,30(a5)
40000178:	f8a42223          	sw	a0,-124(s0)
4000017c:	00a00513          	li	a0,10
40000180:	f8f40f23          	sb	a5,-98(s0)
40000184:	00a00793          	li	a5,10
40000188:	f8e41e23          	sh	a4,-100(s0)
4000018c:	f9142423          	sw	a7,-120(s0)
40000190:	00002717          	auipc	a4,0x2
40000194:	dcf72c23          	sw	a5,-552(a4) # 40001f68 <Arr_2_Glob+0x65c>
40000198:	f9042623          	sw	a6,-116(s0)
4000019c:	f8b42823          	sw	a1,-112(s0)
400001a0:	f8c42a23          	sw	a2,-108(s0)
400001a4:	f8d42c23          	sw	a3,-104(s0)
400001a8:	778000ef          	jal	ra,40000920 <putchar>
400001ac:	00001597          	auipc	a1,0x1
400001b0:	08858593          	addi	a1,a1,136 # 40001234 <__init_array_end+0x20>
400001b4:	00001517          	auipc	a0,0x1
400001b8:	09050513          	addi	a0,a0,144 # 40001244 <__init_array_end+0x30>
400001bc:	025000ef          	jal	ra,400009e0 <printf>
400001c0:	82c18793          	addi	a5,gp,-2004 # 4000182c <Reg>
400001c4:	0007a783          	lw	a5,0(a5)
400001c8:	6e078863          	beqz	a5,400008b8 <main+0x834>
400001cc:	00001517          	auipc	a0,0x1
400001d0:	09c50513          	addi	a0,a0,156 # 40001268 <__init_array_end+0x54>
400001d4:	111000ef          	jal	ra,40000ae4 <puts>
400001d8:	00b72637          	lui	a2,0xb72
400001dc:	b0060613          	addi	a2,a2,-1280 # b71b00 <__stack_size+0xb70b00>
400001e0:	00000693          	li	a3,0
400001e4:	00001597          	auipc	a1,0x1
400001e8:	0e058593          	addi	a1,a1,224 # 400012c4 <__init_array_end+0xb0>
400001ec:	00001517          	auipc	a0,0x1
400001f0:	0e050513          	addi	a0,a0,224 # 400012cc <__init_array_end+0xb8>
400001f4:	7ec000ef          	jal	ra,400009e0 <printf>
400001f8:	00a00513          	li	a0,10
400001fc:	724000ef          	jal	ra,40000920 <putchar>
40000200:	1f400793          	li	a5,500
40000204:	f6f42623          	sw	a5,-148(s0)
40000208:	00001997          	auipc	s3,0x1
4000020c:	5c498993          	addi	s3,s3,1476 # 400017cc <__init_array_end+0x5b8>
40000210:	8201a423          	sw	zero,-2008(gp) # 40001828 <Done>
40000214:	84018d13          	addi	s10,gp,-1984 # 40001840 <Ptr_Glob>
40000218:	00001d97          	auipc	s11,0x1
4000021c:	6f4d8d93          	addi	s11,s11,1780 # 4000190c <Arr_2_Glob>
40000220:	00001497          	auipc	s1,0x1
40000224:	5cc48493          	addi	s1,s1,1484 # 400017ec <__init_array_end+0x5d8>
40000228:	f6c42903          	lw	s2,-148(s0)
4000022c:	00001517          	auipc	a0,0x1
40000230:	0b450513          	addi	a0,a0,180 # 400012e0 <__init_array_end+0xcc>
40000234:	00100a13          	li	s4,1
40000238:	00090593          	mv	a1,s2
4000023c:	7a4000ef          	jal	ra,400009e0 <printf>
40000240:	00100513          	li	a0,1
40000244:	6d4000ef          	jal	ra,40000918 <setStats>
40000248:	00000513          	li	a0,0
4000024c:	6d0000ef          	jal	ra,4000091c <time>
40000250:	00190c93          	addi	s9,s2,1
40000254:	82a1a223          	sw	a0,-2012(gp) # 40001824 <Begin_Time>
40000258:	00200913          	li	s2,2
4000025c:	00100c13          	li	s8,1
40000260:	595000ef          	jal	ra,40000ff4 <Proc_5>
40000264:	569000ef          	jal	ra,40000fcc <Proc_4>
40000268:	0149a603          	lw	a2,20(s3)
4000026c:	01e9c783          	lbu	a5,30(s3)
40000270:	0009ae83          	lw	t4,0(s3)
40000274:	0049ae03          	lw	t3,4(s3)
40000278:	0089a303          	lw	t1,8(s3)
4000027c:	00c9a883          	lw	a7,12(s3)
40000280:	0109a803          	lw	a6,16(s3)
40000284:	0189a683          	lw	a3,24(s3)
40000288:	01c9d703          	lhu	a4,28(s3)
4000028c:	fa040593          	addi	a1,s0,-96
40000290:	f8040513          	addi	a0,s0,-128
40000294:	fac42a23          	sw	a2,-76(s0)
40000298:	faf40f23          	sb	a5,-66(s0)
4000029c:	f7242a23          	sw	s2,-140(s0)
400002a0:	fbd42023          	sw	t4,-96(s0)
400002a4:	fbc42223          	sw	t3,-92(s0)
400002a8:	fa642423          	sw	t1,-88(s0)
400002ac:	fb142623          	sw	a7,-84(s0)
400002b0:	fb042823          	sw	a6,-80(s0)
400002b4:	fad42c23          	sw	a3,-72(s0)
400002b8:	fae41e23          	sh	a4,-68(s0)
400002bc:	f7842e23          	sw	s8,-132(s0)
400002c0:	23d000ef          	jal	ra,40000cfc <Func_2>
400002c4:	f7442603          	lw	a2,-140(s0)
400002c8:	00153513          	seqz	a0,a0
400002cc:	82a1aa23          	sw	a0,-1996(gp) # 40001834 <Bool_Glob>
400002d0:	02c94a63          	blt	s2,a2,40000304 <main+0x280>
400002d4:	00261793          	slli	a5,a2,0x2
400002d8:	00c787b3          	add	a5,a5,a2
400002dc:	ffd78793          	addi	a5,a5,-3
400002e0:	00060513          	mv	a0,a2
400002e4:	00300593          	li	a1,3
400002e8:	f7840613          	addi	a2,s0,-136
400002ec:	f6f42c23          	sw	a5,-136(s0)
400002f0:	179000ef          	jal	ra,40000c68 <Proc_7>
400002f4:	f7442603          	lw	a2,-140(s0)
400002f8:	00160613          	addi	a2,a2,1
400002fc:	f6c42a23          	sw	a2,-140(s0)
40000300:	fcc95ae3          	bge	s2,a2,400002d4 <main+0x250>
40000304:	f7842683          	lw	a3,-136(s0)
40000308:	000d8593          	mv	a1,s11
4000030c:	84418513          	addi	a0,gp,-1980 # 40001844 <Arr_1_Glob>
40000310:	169000ef          	jal	ra,40000c78 <Proc_8>
40000314:	000d2503          	lw	a0,0(s10)
40000318:	04100b13          	li	s6,65
4000031c:	00300a93          	li	s5,3
40000320:	34d000ef          	jal	ra,40000e6c <Proc_1>
40000324:	8301c703          	lbu	a4,-2000(gp) # 40001830 <Ch_2_Glob>
40000328:	04000793          	li	a5,64
4000032c:	02e7f463          	bgeu	a5,a4,40000354 <main+0x2d0>
40000330:	000b0513          	mv	a0,s6
40000334:	04300593          	li	a1,67
40000338:	1a5000ef          	jal	ra,40000cdc <Func_1>
4000033c:	f7c42783          	lw	a5,-132(s0)
40000340:	001b0713          	addi	a4,s6,1
40000344:	4cf50663          	beq	a0,a5,40000810 <main+0x78c>
40000348:	0ff77b13          	andi	s6,a4,255
4000034c:	8301c783          	lbu	a5,-2000(gp) # 40001830 <Ch_2_Glob>
40000350:	ff67f0e3          	bgeu	a5,s6,40000330 <main+0x2ac>
40000354:	f7442783          	lw	a5,-140(s0)
40000358:	f7842b83          	lw	s7,-136(s0)
4000035c:	001a0a13          	addi	s4,s4,1
40000360:	02fa8ab3          	mul	s5,s5,a5
40000364:	f7440513          	addi	a0,s0,-140
40000368:	037acb33          	div	s6,s5,s7
4000036c:	f7642a23          	sw	s6,-140(s0)
40000370:	2a5000ef          	jal	ra,40000e14 <Proc_2>
40000374:	ef9a16e3          	bne	s4,s9,40000260 <main+0x1dc>
40000378:	00000513          	li	a0,0
4000037c:	5a0000ef          	jal	ra,4000091c <time>
40000380:	82a1a023          	sw	a0,-2016(gp) # 40001820 <End_Time>
40000384:	00000513          	li	a0,0
40000388:	590000ef          	jal	ra,40000918 <setStats>
4000038c:	82418713          	addi	a4,gp,-2012 # 40001824 <Begin_Time>
40000390:	82018793          	addi	a5,gp,-2016 # 40001820 <End_Time>
40000394:	00072683          	lw	a3,0(a4)
40000398:	0007a783          	lw	a5,0(a5)
4000039c:	00018737          	lui	a4,0x18
400003a0:	69f70713          	addi	a4,a4,1695 # 1869f <__stack_size+0x1769f>
400003a4:	40d787b3          	sub	a5,a5,a3
400003a8:	80f1ae23          	sw	a5,-2020(gp) # 4000181c <User_Time>
400003ac:	4cf75a63          	bge	a4,a5,40000880 <main+0x7fc>
400003b0:	00100793          	li	a5,1
400003b4:	82f1a423          	sw	a5,-2008(gp) # 40001828 <Done>
400003b8:	00001517          	auipc	a0,0x1
400003bc:	f8450513          	addi	a0,a0,-124 # 4000133c <__init_array_end+0x128>
400003c0:	724000ef          	jal	ra,40000ae4 <puts>
400003c4:	00a00513          	li	a0,10
400003c8:	558000ef          	jal	ra,40000920 <putchar>
400003cc:	83818793          	addi	a5,gp,-1992 # 40001838 <Int_Glob>
400003d0:	0007a583          	lw	a1,0(a5)
400003d4:	00001517          	auipc	a0,0x1
400003d8:	fa050513          	addi	a0,a0,-96 # 40001374 <__init_array_end+0x160>
400003dc:	83c18493          	addi	s1,gp,-1988 # 4000183c <Next_Ptr_Glob>
400003e0:	600000ef          	jal	ra,400009e0 <printf>
400003e4:	00500593          	li	a1,5
400003e8:	00001517          	auipc	a0,0x1
400003ec:	fa850513          	addi	a0,a0,-88 # 40001390 <__init_array_end+0x17c>
400003f0:	5f0000ef          	jal	ra,400009e0 <printf>
400003f4:	83418793          	addi	a5,gp,-1996 # 40001834 <Bool_Glob>
400003f8:	0007a583          	lw	a1,0(a5)
400003fc:	00001517          	auipc	a0,0x1
40000400:	fb050513          	addi	a0,a0,-80 # 400013ac <__init_array_end+0x198>
40000404:	417a8ab3          	sub	s5,s5,s7
40000408:	5d8000ef          	jal	ra,400009e0 <printf>
4000040c:	00100593          	li	a1,1
40000410:	00001517          	auipc	a0,0x1
40000414:	f8050513          	addi	a0,a0,-128 # 40001390 <__init_array_end+0x17c>
40000418:	5c8000ef          	jal	ra,400009e0 <printf>
4000041c:	8311c583          	lbu	a1,-1999(gp) # 40001831 <Ch_1_Glob>
40000420:	00001517          	auipc	a0,0x1
40000424:	fa850513          	addi	a0,a0,-88 # 400013c8 <__init_array_end+0x1b4>
40000428:	5b8000ef          	jal	ra,400009e0 <printf>
4000042c:	04100593          	li	a1,65
40000430:	00001517          	auipc	a0,0x1
40000434:	fb450513          	addi	a0,a0,-76 # 400013e4 <__init_array_end+0x1d0>
40000438:	5a8000ef          	jal	ra,400009e0 <printf>
4000043c:	8301c583          	lbu	a1,-2000(gp) # 40001830 <Ch_2_Glob>
40000440:	00001517          	auipc	a0,0x1
40000444:	fc050513          	addi	a0,a0,-64 # 40001400 <__init_array_end+0x1ec>
40000448:	598000ef          	jal	ra,400009e0 <printf>
4000044c:	04200593          	li	a1,66
40000450:	00001517          	auipc	a0,0x1
40000454:	f9450513          	addi	a0,a0,-108 # 400013e4 <__init_array_end+0x1d0>
40000458:	588000ef          	jal	ra,400009e0 <printf>
4000045c:	84418793          	addi	a5,gp,-1980 # 40001844 <Arr_1_Glob>
40000460:	0207a583          	lw	a1,32(a5)
40000464:	00001517          	auipc	a0,0x1
40000468:	fb850513          	addi	a0,a0,-72 # 4000141c <__init_array_end+0x208>
4000046c:	81c18913          	addi	s2,gp,-2020 # 4000181c <User_Time>
40000470:	570000ef          	jal	ra,400009e0 <printf>
40000474:	00700593          	li	a1,7
40000478:	00001517          	auipc	a0,0x1
4000047c:	f1850513          	addi	a0,a0,-232 # 40001390 <__init_array_end+0x17c>
40000480:	560000ef          	jal	ra,400009e0 <printf>
40000484:	00001797          	auipc	a5,0x1
40000488:	48878793          	addi	a5,a5,1160 # 4000190c <Arr_2_Glob>
4000048c:	65c7a583          	lw	a1,1628(a5)
40000490:	00001517          	auipc	a0,0x1
40000494:	fa850513          	addi	a0,a0,-88 # 40001438 <__init_array_end+0x224>
40000498:	548000ef          	jal	ra,400009e0 <printf>
4000049c:	00001517          	auipc	a0,0x1
400004a0:	fb850513          	addi	a0,a0,-72 # 40001454 <__init_array_end+0x240>
400004a4:	640000ef          	jal	ra,40000ae4 <puts>
400004a8:	00001517          	auipc	a0,0x1
400004ac:	fd850513          	addi	a0,a0,-40 # 40001480 <__init_array_end+0x26c>
400004b0:	634000ef          	jal	ra,40000ae4 <puts>
400004b4:	000d2783          	lw	a5,0(s10)
400004b8:	00001517          	auipc	a0,0x1
400004bc:	fd450513          	addi	a0,a0,-44 # 4000148c <__init_array_end+0x278>
400004c0:	0007a583          	lw	a1,0(a5)
400004c4:	51c000ef          	jal	ra,400009e0 <printf>
400004c8:	00001517          	auipc	a0,0x1
400004cc:	fe050513          	addi	a0,a0,-32 # 400014a8 <__init_array_end+0x294>
400004d0:	614000ef          	jal	ra,40000ae4 <puts>
400004d4:	000d2783          	lw	a5,0(s10)
400004d8:	00001517          	auipc	a0,0x1
400004dc:	00050513          	mv	a0,a0
400004e0:	0047a583          	lw	a1,4(a5)
400004e4:	4fc000ef          	jal	ra,400009e0 <printf>
400004e8:	00000593          	li	a1,0
400004ec:	00001517          	auipc	a0,0x1
400004f0:	ea450513          	addi	a0,a0,-348 # 40001390 <__init_array_end+0x17c>
400004f4:	4ec000ef          	jal	ra,400009e0 <printf>
400004f8:	000d2783          	lw	a5,0(s10)
400004fc:	00001517          	auipc	a0,0x1
40000500:	ff850513          	addi	a0,a0,-8 # 400014f4 <__init_array_end+0x2e0>
40000504:	0087a583          	lw	a1,8(a5)
40000508:	4d8000ef          	jal	ra,400009e0 <printf>
4000050c:	00200593          	li	a1,2
40000510:	00001517          	auipc	a0,0x1
40000514:	e8050513          	addi	a0,a0,-384 # 40001390 <__init_array_end+0x17c>
40000518:	4c8000ef          	jal	ra,400009e0 <printf>
4000051c:	000d2783          	lw	a5,0(s10)
40000520:	00001517          	auipc	a0,0x1
40000524:	ff050513          	addi	a0,a0,-16 # 40001510 <__init_array_end+0x2fc>
40000528:	00c7a583          	lw	a1,12(a5)
4000052c:	4b4000ef          	jal	ra,400009e0 <printf>
40000530:	01100593          	li	a1,17
40000534:	00001517          	auipc	a0,0x1
40000538:	e5c50513          	addi	a0,a0,-420 # 40001390 <__init_array_end+0x17c>
4000053c:	4a4000ef          	jal	ra,400009e0 <printf>
40000540:	000d2583          	lw	a1,0(s10)
40000544:	00001517          	auipc	a0,0x1
40000548:	fe850513          	addi	a0,a0,-24 # 4000152c <__init_array_end+0x318>
4000054c:	01058593          	addi	a1,a1,16
40000550:	490000ef          	jal	ra,400009e0 <printf>
40000554:	00001517          	auipc	a0,0x1
40000558:	ff450513          	addi	a0,a0,-12 # 40001548 <__init_array_end+0x334>
4000055c:	588000ef          	jal	ra,40000ae4 <puts>
40000560:	00001517          	auipc	a0,0x1
40000564:	01c50513          	addi	a0,a0,28 # 4000157c <__init_array_end+0x368>
40000568:	57c000ef          	jal	ra,40000ae4 <puts>
4000056c:	0004a783          	lw	a5,0(s1)
40000570:	00001517          	auipc	a0,0x1
40000574:	f1c50513          	addi	a0,a0,-228 # 4000148c <__init_array_end+0x278>
40000578:	0007a583          	lw	a1,0(a5)
4000057c:	464000ef          	jal	ra,400009e0 <printf>
40000580:	00001517          	auipc	a0,0x1
40000584:	00c50513          	addi	a0,a0,12 # 4000158c <__init_array_end+0x378>
40000588:	55c000ef          	jal	ra,40000ae4 <puts>
4000058c:	0004a783          	lw	a5,0(s1)
40000590:	00001517          	auipc	a0,0x1
40000594:	f4850513          	addi	a0,a0,-184 # 400014d8 <__init_array_end+0x2c4>
40000598:	0047a583          	lw	a1,4(a5)
4000059c:	444000ef          	jal	ra,400009e0 <printf>
400005a0:	00000593          	li	a1,0
400005a4:	00001517          	auipc	a0,0x1
400005a8:	dec50513          	addi	a0,a0,-532 # 40001390 <__init_array_end+0x17c>
400005ac:	434000ef          	jal	ra,400009e0 <printf>
400005b0:	0004a783          	lw	a5,0(s1)
400005b4:	00001517          	auipc	a0,0x1
400005b8:	f4050513          	addi	a0,a0,-192 # 400014f4 <__init_array_end+0x2e0>
400005bc:	0087a583          	lw	a1,8(a5)
400005c0:	420000ef          	jal	ra,400009e0 <printf>
400005c4:	00100593          	li	a1,1
400005c8:	00001517          	auipc	a0,0x1
400005cc:	dc850513          	addi	a0,a0,-568 # 40001390 <__init_array_end+0x17c>
400005d0:	410000ef          	jal	ra,400009e0 <printf>
400005d4:	0004a783          	lw	a5,0(s1)
400005d8:	00001517          	auipc	a0,0x1
400005dc:	f3850513          	addi	a0,a0,-200 # 40001510 <__init_array_end+0x2fc>
400005e0:	00c7a583          	lw	a1,12(a5)
400005e4:	3fc000ef          	jal	ra,400009e0 <printf>
400005e8:	01200593          	li	a1,18
400005ec:	00001517          	auipc	a0,0x1
400005f0:	da450513          	addi	a0,a0,-604 # 40001390 <__init_array_end+0x17c>
400005f4:	3ec000ef          	jal	ra,400009e0 <printf>
400005f8:	0004a583          	lw	a1,0(s1)
400005fc:	00001517          	auipc	a0,0x1
40000600:	f3050513          	addi	a0,a0,-208 # 4000152c <__init_array_end+0x318>
40000604:	00b724b7          	lui	s1,0xb72
40000608:	01058593          	addi	a1,a1,16
4000060c:	3d4000ef          	jal	ra,400009e0 <printf>
40000610:	00001517          	auipc	a0,0x1
40000614:	f3850513          	addi	a0,a0,-200 # 40001548 <__init_array_end+0x334>
40000618:	4cc000ef          	jal	ra,40000ae4 <puts>
4000061c:	f7442583          	lw	a1,-140(s0)
40000620:	00001517          	auipc	a0,0x1
40000624:	fac50513          	addi	a0,a0,-84 # 400015cc <__init_array_end+0x3b8>
40000628:	b0048493          	addi	s1,s1,-1280 # b71b00 <__stack_size+0xb70b00>
4000062c:	3b4000ef          	jal	ra,400009e0 <printf>
40000630:	00500593          	li	a1,5
40000634:	00001517          	auipc	a0,0x1
40000638:	d5c50513          	addi	a0,a0,-676 # 40001390 <__init_array_end+0x17c>
4000063c:	3a4000ef          	jal	ra,400009e0 <printf>
40000640:	003a9793          	slli	a5,s5,0x3
40000644:	41578ab3          	sub	s5,a5,s5
40000648:	416a85b3          	sub	a1,s5,s6
4000064c:	00001517          	auipc	a0,0x1
40000650:	f9c50513          	addi	a0,a0,-100 # 400015e8 <__init_array_end+0x3d4>
40000654:	38c000ef          	jal	ra,400009e0 <printf>
40000658:	00d00593          	li	a1,13
4000065c:	00001517          	auipc	a0,0x1
40000660:	d3450513          	addi	a0,a0,-716 # 40001390 <__init_array_end+0x17c>
40000664:	37c000ef          	jal	ra,400009e0 <printf>
40000668:	f7842583          	lw	a1,-136(s0)
4000066c:	00001517          	auipc	a0,0x1
40000670:	f9850513          	addi	a0,a0,-104 # 40001604 <__init_array_end+0x3f0>
40000674:	36c000ef          	jal	ra,400009e0 <printf>
40000678:	00700593          	li	a1,7
4000067c:	00001517          	auipc	a0,0x1
40000680:	d1450513          	addi	a0,a0,-748 # 40001390 <__init_array_end+0x17c>
40000684:	35c000ef          	jal	ra,400009e0 <printf>
40000688:	f7c42583          	lw	a1,-132(s0)
4000068c:	00001517          	auipc	a0,0x1
40000690:	f9450513          	addi	a0,a0,-108 # 40001620 <__init_array_end+0x40c>
40000694:	34c000ef          	jal	ra,400009e0 <printf>
40000698:	00100593          	li	a1,1
4000069c:	00001517          	auipc	a0,0x1
400006a0:	cf450513          	addi	a0,a0,-780 # 40001390 <__init_array_end+0x17c>
400006a4:	33c000ef          	jal	ra,400009e0 <printf>
400006a8:	f8040593          	addi	a1,s0,-128
400006ac:	00001517          	auipc	a0,0x1
400006b0:	f9050513          	addi	a0,a0,-112 # 4000163c <__init_array_end+0x428>
400006b4:	32c000ef          	jal	ra,400009e0 <printf>
400006b8:	00001517          	auipc	a0,0x1
400006bc:	fa050513          	addi	a0,a0,-96 # 40001658 <__init_array_end+0x444>
400006c0:	424000ef          	jal	ra,40000ae4 <puts>
400006c4:	fa040593          	addi	a1,s0,-96
400006c8:	00001517          	auipc	a0,0x1
400006cc:	fc450513          	addi	a0,a0,-60 # 4000168c <__init_array_end+0x478>
400006d0:	310000ef          	jal	ra,400009e0 <printf>
400006d4:	00001517          	auipc	a0,0x1
400006d8:	fd450513          	addi	a0,a0,-44 # 400016a8 <__init_array_end+0x494>
400006dc:	408000ef          	jal	ra,40000ae4 <puts>
400006e0:	00a00513          	li	a0,10
400006e4:	23c000ef          	jal	ra,40000920 <putchar>
400006e8:	00092603          	lw	a2,0(s2)
400006ec:	f6c42a03          	lw	s4,-148(s0)
400006f0:	00c00793          	li	a5,12
400006f4:	41f65693          	srai	a3,a2,0x1f
400006f8:	034649b3          	div	s3,a2,s4
400006fc:	02f9c9b3          	div	s3,s3,a5
40000700:	029a15b3          	mulh	a1,s4,s1
40000704:	8131ac23          	sw	s3,-2024(gp) # 40001818 <Microseconds>
40000708:	029a0533          	mul	a0,s4,s1
4000070c:	41c000ef          	jal	ra,40000b28 <__divdi3>
40000710:	80a1aa23          	sw	a0,-2028(gp) # 40001814 <Dhrystones_Per_Second>
40000714:	00098593          	mv	a1,s3
40000718:	00001517          	auipc	a0,0x1
4000071c:	fc450513          	addi	a0,a0,-60 # 400016dc <__init_array_end+0x4c8>
40000720:	2c0000ef          	jal	ra,400009e0 <printf>
40000724:	81418793          	addi	a5,gp,-2028 # 40001814 <Dhrystones_Per_Second>
40000728:	0007a583          	lw	a1,0(a5)
4000072c:	00001517          	auipc	a0,0x1
40000730:	fe450513          	addi	a0,a0,-28 # 40001710 <__init_array_end+0x4fc>
40000734:	2ac000ef          	jal	ra,400009e0 <printf>
40000738:	00092583          	lw	a1,0(s2)
4000073c:	00001517          	auipc	a0,0x1
40000740:	00850513          	addi	a0,a0,8 # 40001744 <__init_array_end+0x530>
40000744:	29c000ef          	jal	ra,400009e0 <printf>
40000748:	000a0593          	mv	a1,s4
4000074c:	00001517          	auipc	a0,0x1
40000750:	00850513          	addi	a0,a0,8 # 40001754 <__init_array_end+0x540>
40000754:	28c000ef          	jal	ra,400009e0 <printf>
40000758:	00048593          	mv	a1,s1
4000075c:	00001517          	auipc	a0,0x1
40000760:	01050513          	addi	a0,a0,16 # 4000176c <__init_array_end+0x558>
40000764:	27c000ef          	jal	ra,400009e0 <printf>
40000768:	00001517          	auipc	a0,0x1
4000076c:	01050513          	addi	a0,a0,16 # 40001778 <__init_array_end+0x564>
40000770:	270000ef          	jal	ra,400009e0 <printf>
40000774:	0000e537          	lui	a0,0xe
40000778:	e4450513          	addi	a0,a0,-444 # de44 <__stack_size+0xce44>
4000077c:	02aa15b3          	mulh	a1,s4,a0
40000780:	00092603          	lw	a2,0(s2)
40000784:	f9c00493          	li	s1,-100
40000788:	41f65693          	srai	a3,a2,0x1f
4000078c:	02aa0533          	mul	a0,s4,a0
40000790:	398000ef          	jal	ra,40000b28 <__divdi3>
40000794:	00050793          	mv	a5,a0
40000798:	06400593          	li	a1,100
4000079c:	02b7c5b3          	div	a1,a5,a1
400007a0:	00001517          	auipc	a0,0x1
400007a4:	00850513          	addi	a0,a0,8 # 400017a8 <__init_array_end+0x594>
400007a8:	029584b3          	mul	s1,a1,s1
400007ac:	00f484b3          	add	s1,s1,a5
400007b0:	230000ef          	jal	ra,400009e0 <printf>
400007b4:	00900793          	li	a5,9
400007b8:	1097d863          	bge	a5,s1,400008c8 <main+0x844>
400007bc:	00048593          	mv	a1,s1
400007c0:	00001517          	auipc	a0,0x1
400007c4:	f9050513          	addi	a0,a0,-112 # 40001750 <__init_array_end+0x53c>
400007c8:	218000ef          	jal	ra,400009e0 <printf>
400007cc:	f6040113          	addi	sp,s0,-160
400007d0:	09c12083          	lw	ra,156(sp)
400007d4:	00000513          	li	a0,0
400007d8:	09812403          	lw	s0,152(sp)
400007dc:	09412483          	lw	s1,148(sp)
400007e0:	09012903          	lw	s2,144(sp)
400007e4:	08c12983          	lw	s3,140(sp)
400007e8:	08812a03          	lw	s4,136(sp)
400007ec:	08412a83          	lw	s5,132(sp)
400007f0:	08012b03          	lw	s6,128(sp)
400007f4:	07c12b83          	lw	s7,124(sp)
400007f8:	07812c03          	lw	s8,120(sp)
400007fc:	07412c83          	lw	s9,116(sp)
40000800:	07012d03          	lw	s10,112(sp)
40000804:	06c12d83          	lw	s11,108(sp)
40000808:	0a010113          	addi	sp,sp,160
4000080c:	00008067          	ret
40000810:	f7c40593          	addi	a1,s0,-132
40000814:	00000513          	li	a0,0
40000818:	550000ef          	jal	ra,40000d68 <Proc_6>
4000081c:	01e4c783          	lbu	a5,30(s1)
40000820:	0004a303          	lw	t1,0(s1)
40000824:	0044a883          	lw	a7,4(s1)
40000828:	0084a803          	lw	a6,8(s1)
4000082c:	00c4a503          	lw	a0,12(s1)
40000830:	0104a583          	lw	a1,16(s1)
40000834:	0144a603          	lw	a2,20(s1)
40000838:	0184a683          	lw	a3,24(s1)
4000083c:	01c4d703          	lhu	a4,28(s1)
40000840:	faf40f23          	sb	a5,-66(s0)
40000844:	001b0b13          	addi	s6,s6,1
40000848:	8341ac23          	sw	s4,-1992(gp) # 40001838 <Int_Glob>
4000084c:	fa642023          	sw	t1,-96(s0)
40000850:	fb142223          	sw	a7,-92(s0)
40000854:	fb042423          	sw	a6,-88(s0)
40000858:	faa42623          	sw	a0,-84(s0)
4000085c:	fab42823          	sw	a1,-80(s0)
40000860:	fac42a23          	sw	a2,-76(s0)
40000864:	fad42c23          	sw	a3,-72(s0)
40000868:	fae41e23          	sh	a4,-68(s0)
4000086c:	0ffb7b13          	andi	s6,s6,255
40000870:	8301c783          	lbu	a5,-2000(gp) # 40001830 <Ch_2_Glob>
40000874:	000a0a93          	mv	s5,s4
40000878:	ab67fce3          	bgeu	a5,s6,40000330 <main+0x2ac>
4000087c:	ad9ff06f          	j	40000354 <main+0x2d0>
40000880:	00001517          	auipc	a0,0x1
40000884:	a8450513          	addi	a0,a0,-1404 # 40001304 <__init_array_end+0xf0>
40000888:	25c000ef          	jal	ra,40000ae4 <puts>
4000088c:	f6c42703          	lw	a4,-148(s0)
40000890:	00a00513          	li	a0,10
40000894:	00271793          	slli	a5,a4,0x2
40000898:	00e787b3          	add	a5,a5,a4
4000089c:	00179793          	slli	a5,a5,0x1
400008a0:	f6f42623          	sw	a5,-148(s0)
400008a4:	07c000ef          	jal	ra,40000920 <putchar>
400008a8:	82818793          	addi	a5,gp,-2008 # 40001828 <Done>
400008ac:	0007a783          	lw	a5,0(a5)
400008b0:	96078ce3          	beqz	a5,40000228 <main+0x1a4>
400008b4:	b05ff06f          	j	400003b8 <main+0x334>
400008b8:	00001517          	auipc	a0,0x1
400008bc:	9dc50513          	addi	a0,a0,-1572 # 40001294 <__init_array_end+0x80>
400008c0:	224000ef          	jal	ra,40000ae4 <puts>
400008c4:	915ff06f          	j	400001d8 <main+0x154>
400008c8:	03000513          	li	a0,48
400008cc:	054000ef          	jal	ra,40000920 <putchar>
400008d0:	eedff06f          	j	400007bc <main+0x738>

400008d4 <write_u32.constprop.0>:
400008d4:	100007b7          	lui	a5,0x10000
400008d8:	00a7a023          	sw	a0,0(a5) # 10000000 <__stack_size+0xffff000>
400008dc:	00008067          	ret

400008e0 <sim_putchar>:
400008e0:	ff010113          	addi	sp,sp,-16
400008e4:	00812423          	sw	s0,8(sp)
400008e8:	00112623          	sw	ra,12(sp)
400008ec:	00050413          	mv	s0,a0
400008f0:	fe5ff0ef          	jal	ra,400008d4 <write_u32.constprop.0>
400008f4:	00040513          	mv	a0,s0
400008f8:	00c12083          	lw	ra,12(sp)
400008fc:	00812403          	lw	s0,8(sp)
40000900:	01010113          	addi	sp,sp,16
40000904:	00008067          	ret

40000908 <read_u32.constprop.1>:
40000908:	1001c7b7          	lui	a5,0x1001c
4000090c:	ff87a503          	lw	a0,-8(a5) # 1001bff8 <__stack_size+0x1001aff8>
40000910:	00008067          	ret

40000914 <sim_time>:
40000914:	ff5ff06f          	j	40000908 <read_u32.constprop.1>

40000918 <setStats>:
40000918:	00008067          	ret

4000091c <time>:
4000091c:	ff9ff06f          	j	40000914 <sim_time>

40000920 <putchar>:
40000920:	fc1ff06f          	j	400008e0 <sim_putchar>

40000924 <printf_s>:
40000924:	ff010113          	addi	sp,sp,-16
40000928:	00812423          	sw	s0,8(sp)
4000092c:	00112623          	sw	ra,12(sp)
40000930:	00050413          	mv	s0,a0
40000934:	00054503          	lbu	a0,0(a0)
40000938:	00050a63          	beqz	a0,4000094c <printf_s+0x28>
4000093c:	00140413          	addi	s0,s0,1
40000940:	fe1ff0ef          	jal	ra,40000920 <putchar>
40000944:	00044503          	lbu	a0,0(s0)
40000948:	fe051ae3          	bnez	a0,4000093c <printf_s+0x18>
4000094c:	00c12083          	lw	ra,12(sp)
40000950:	00812403          	lw	s0,8(sp)
40000954:	01010113          	addi	sp,sp,16
40000958:	00008067          	ret

4000095c <printf_c>:
4000095c:	fc5ff06f          	j	40000920 <putchar>

40000960 <printf_d>:
40000960:	fd010113          	addi	sp,sp,-48
40000964:	02912223          	sw	s1,36(sp)
40000968:	02112623          	sw	ra,44(sp)
4000096c:	02812423          	sw	s0,40(sp)
40000970:	03212023          	sw	s2,32(sp)
40000974:	00050493          	mv	s1,a0
40000978:	04054c63          	bltz	a0,400009d0 <printf_d+0x70>
4000097c:	00010913          	mv	s2,sp
40000980:	00090413          	mv	s0,s2
40000984:	00a00713          	li	a4,10
40000988:	00049463          	bnez	s1,40000990 <printf_d+0x30>
4000098c:	01241e63          	bne	s0,s2,400009a8 <printf_d+0x48>
40000990:	02e4e7b3          	rem	a5,s1,a4
40000994:	00140413          	addi	s0,s0,1
40000998:	03078793          	addi	a5,a5,48
4000099c:	02e4c4b3          	div	s1,s1,a4
400009a0:	fef40fa3          	sb	a5,-1(s0)
400009a4:	fe5ff06f          	j	40000988 <printf_d+0x28>
400009a8:	fff40413          	addi	s0,s0,-1
400009ac:	00044503          	lbu	a0,0(s0)
400009b0:	fadff0ef          	jal	ra,4000095c <printf_c>
400009b4:	ff241ae3          	bne	s0,s2,400009a8 <printf_d+0x48>
400009b8:	02c12083          	lw	ra,44(sp)
400009bc:	02812403          	lw	s0,40(sp)
400009c0:	02412483          	lw	s1,36(sp)
400009c4:	02012903          	lw	s2,32(sp)
400009c8:	03010113          	addi	sp,sp,48
400009cc:	00008067          	ret
400009d0:	02d00513          	li	a0,45
400009d4:	f89ff0ef          	jal	ra,4000095c <printf_c>
400009d8:	409004b3          	neg	s1,s1
400009dc:	fa1ff06f          	j	4000097c <printf_d+0x1c>

400009e0 <printf>:
400009e0:	fb010113          	addi	sp,sp,-80
400009e4:	02912223          	sw	s1,36(sp)
400009e8:	02112623          	sw	ra,44(sp)
400009ec:	02812423          	sw	s0,40(sp)
400009f0:	03212023          	sw	s2,32(sp)
400009f4:	01312e23          	sw	s3,28(sp)
400009f8:	01412c23          	sw	s4,24(sp)
400009fc:	01512a23          	sw	s5,20(sp)
40000a00:	00050493          	mv	s1,a0
40000a04:	00054503          	lbu	a0,0(a0)
40000a08:	04f12223          	sw	a5,68(sp)
40000a0c:	03410793          	addi	a5,sp,52
40000a10:	02b12a23          	sw	a1,52(sp)
40000a14:	02c12c23          	sw	a2,56(sp)
40000a18:	02d12e23          	sw	a3,60(sp)
40000a1c:	04e12023          	sw	a4,64(sp)
40000a20:	05012423          	sw	a6,72(sp)
40000a24:	05112623          	sw	a7,76(sp)
40000a28:	00f12623          	sw	a5,12(sp)
40000a2c:	02050863          	beqz	a0,40000a5c <printf+0x7c>
40000a30:	00000413          	li	s0,0
40000a34:	02500a93          	li	s5,37
40000a38:	06300913          	li	s2,99
40000a3c:	07300993          	li	s3,115
40000a40:	06400a13          	li	s4,100
40000a44:	05550263          	beq	a0,s5,40000a88 <printf+0xa8>
40000a48:	f15ff0ef          	jal	ra,4000095c <printf_c>
40000a4c:	00140413          	addi	s0,s0,1
40000a50:	008487b3          	add	a5,s1,s0
40000a54:	0007c503          	lbu	a0,0(a5)
40000a58:	fe0516e3          	bnez	a0,40000a44 <printf+0x64>
40000a5c:	02c12083          	lw	ra,44(sp)
40000a60:	02812403          	lw	s0,40(sp)
40000a64:	02412483          	lw	s1,36(sp)
40000a68:	02012903          	lw	s2,32(sp)
40000a6c:	01c12983          	lw	s3,28(sp)
40000a70:	01812a03          	lw	s4,24(sp)
40000a74:	01412a83          	lw	s5,20(sp)
40000a78:	05010113          	addi	sp,sp,80
40000a7c:	00008067          	ret
40000a80:	03378a63          	beq	a5,s3,40000ab4 <printf+0xd4>
40000a84:	05478463          	beq	a5,s4,40000acc <printf+0xec>
40000a88:	00140413          	addi	s0,s0,1
40000a8c:	008487b3          	add	a5,s1,s0
40000a90:	0007c783          	lbu	a5,0(a5)
40000a94:	fa078ce3          	beqz	a5,40000a4c <printf+0x6c>
40000a98:	ff2794e3          	bne	a5,s2,40000a80 <printf+0xa0>
40000a9c:	00c12783          	lw	a5,12(sp)
40000aa0:	0007a503          	lw	a0,0(a5)
40000aa4:	00478793          	addi	a5,a5,4
40000aa8:	00f12623          	sw	a5,12(sp)
40000aac:	eb1ff0ef          	jal	ra,4000095c <printf_c>
40000ab0:	f9dff06f          	j	40000a4c <printf+0x6c>
40000ab4:	00c12783          	lw	a5,12(sp)
40000ab8:	0007a503          	lw	a0,0(a5)
40000abc:	00478793          	addi	a5,a5,4
40000ac0:	00f12623          	sw	a5,12(sp)
40000ac4:	e61ff0ef          	jal	ra,40000924 <printf_s>
40000ac8:	f85ff06f          	j	40000a4c <printf+0x6c>
40000acc:	00c12783          	lw	a5,12(sp)
40000ad0:	0007a503          	lw	a0,0(a5)
40000ad4:	00478793          	addi	a5,a5,4
40000ad8:	00f12623          	sw	a5,12(sp)
40000adc:	e85ff0ef          	jal	ra,40000960 <printf_d>
40000ae0:	f6dff06f          	j	40000a4c <printf+0x6c>

40000ae4 <puts>:
40000ae4:	ff010113          	addi	sp,sp,-16
40000ae8:	00812423          	sw	s0,8(sp)
40000aec:	00112623          	sw	ra,12(sp)
40000af0:	00050413          	mv	s0,a0
40000af4:	00054503          	lbu	a0,0(a0)
40000af8:	00050a63          	beqz	a0,40000b0c <puts+0x28>
40000afc:	00140413          	addi	s0,s0,1
40000b00:	e21ff0ef          	jal	ra,40000920 <putchar>
40000b04:	00044503          	lbu	a0,0(s0)
40000b08:	fe051ae3          	bnez	a0,40000afc <puts+0x18>
40000b0c:	00a00513          	li	a0,10
40000b10:	e11ff0ef          	jal	ra,40000920 <putchar>
40000b14:	00c12083          	lw	ra,12(sp)
40000b18:	00812403          	lw	s0,8(sp)
40000b1c:	00000513          	li	a0,0
40000b20:	01010113          	addi	sp,sp,16
40000b24:	00008067          	ret

40000b28 <__divdi3>:
40000b28:	fd010113          	addi	sp,sp,-48
40000b2c:	01412e23          	sw	s4,28(sp)
40000b30:	01512c23          	sw	s5,24(sp)
40000b34:	02812623          	sw	s0,44(sp)
40000b38:	02912423          	sw	s1,40(sp)
40000b3c:	03212223          	sw	s2,36(sp)
40000b40:	03312023          	sw	s3,32(sp)
40000b44:	01612a23          	sw	s6,20(sp)
40000b48:	01712823          	sw	s7,16(sp)
40000b4c:	01812623          	sw	s8,12(sp)
40000b50:	00050a13          	mv	s4,a0
40000b54:	00058a93          	mv	s5,a1
40000b58:	00000793          	li	a5,0
40000b5c:	00000713          	li	a4,0
40000b60:	0206c463          	bltz	a3,40000b88 <__divdi3+0x60>
40000b64:	01f65313          	srli	t1,a2,0x1f
40000b68:	00178813          	addi	a6,a5,1
40000b6c:	00169693          	slli	a3,a3,0x1
40000b70:	00f838b3          	sltu	a7,a6,a5
40000b74:	00d366b3          	or	a3,t1,a3
40000b78:	00161613          	slli	a2,a2,0x1
40000b7c:	00080793          	mv	a5,a6
40000b80:	00e88733          	add	a4,a7,a4
40000b84:	fe06d0e3          	bgez	a3,40000b64 <__divdi3+0x3c>
40000b88:	015a6833          	or	a6,s4,s5
40000b8c:	00000513          	li	a0,0
40000b90:	00000593          	li	a1,0
40000b94:	00100c13          	li	s8,1
40000b98:	0c080263          	beqz	a6,40000c5c <__divdi3+0x134>
40000b9c:	fff78813          	addi	a6,a5,-1
40000ba0:	00e7e8b3          	or	a7,a5,a4
40000ba4:	01f69e93          	slli	t4,a3,0x1f
40000ba8:	00f83f33          	sltu	t5,a6,a5
40000bac:	00165f93          	srli	t6,a2,0x1
40000bb0:	fff70293          	addi	t0,a4,-1
40000bb4:	08dae863          	bltu	s5,a3,40000c44 <__divdi3+0x11c>
40000bb8:	00fc1333          	sll	t1,s8,a5
40000bbc:	40ca0e33          	sub	t3,s4,a2
40000bc0:	00080393          	mv	t2,a6
40000bc4:	01ca3933          	sltu	s2,s4,t3
40000bc8:	40da89b3          	sub	s3,s5,a3
40000bcc:	00088413          	mv	s0,a7
40000bd0:	41f35493          	srai	s1,t1,0x1f
40000bd4:	000e8b93          	mv	s7,t4
40000bd8:	000f0b13          	mv	s6,t5
40000bdc:	000f8793          	mv	a5,t6
40000be0:	00028713          	mv	a4,t0
40000be4:	01569463          	bne	a3,s5,40000bec <__divdi3+0xc4>
40000be8:	04ca6e63          	bltu	s4,a2,40000c44 <__divdi3+0x11c>
40000bec:	41298ab3          	sub	s5,s3,s2
40000bf0:	000e0a13          	mv	s4,t3
40000bf4:	015e6833          	or	a6,t3,s5
40000bf8:	00656533          	or	a0,a0,t1
40000bfc:	0095e5b3          	or	a1,a1,s1
40000c00:	00040c63          	beqz	s0,40000c18 <__divdi3+0xf0>
40000c04:	00fbe633          	or	a2,s7,a5
40000c08:	0016d693          	srli	a3,a3,0x1
40000c0c:	00038793          	mv	a5,t2
40000c10:	00eb0733          	add	a4,s6,a4
40000c14:	f80814e3          	bnez	a6,40000b9c <__divdi3+0x74>
40000c18:	02c12403          	lw	s0,44(sp)
40000c1c:	02812483          	lw	s1,40(sp)
40000c20:	02412903          	lw	s2,36(sp)
40000c24:	02012983          	lw	s3,32(sp)
40000c28:	01c12a03          	lw	s4,28(sp)
40000c2c:	01812a83          	lw	s5,24(sp)
40000c30:	01412b03          	lw	s6,20(sp)
40000c34:	01012b83          	lw	s7,16(sp)
40000c38:	00c12c03          	lw	s8,12(sp)
40000c3c:	03010113          	addi	sp,sp,48
40000c40:	00008067          	ret
40000c44:	fc088ae3          	beqz	a7,40000c18 <__divdi3+0xf0>
40000c48:	01fee633          	or	a2,t4,t6
40000c4c:	0016d693          	srli	a3,a3,0x1
40000c50:	00080793          	mv	a5,a6
40000c54:	005f0733          	add	a4,t5,t0
40000c58:	f45ff06f          	j	40000b9c <__divdi3+0x74>
40000c5c:	000a0513          	mv	a0,s4
40000c60:	000a8593          	mv	a1,s5
40000c64:	fb5ff06f          	j	40000c18 <__divdi3+0xf0>

40000c68 <Proc_7>:
40000c68:	00250513          	addi	a0,a0,2
40000c6c:	00b505b3          	add	a1,a0,a1
40000c70:	00b62023          	sw	a1,0(a2)
40000c74:	00008067          	ret

40000c78 <Proc_8>:
40000c78:	00560713          	addi	a4,a2,5
40000c7c:	0c800813          	li	a6,200
40000c80:	03070833          	mul	a6,a4,a6
40000c84:	00261613          	slli	a2,a2,0x2
40000c88:	00271793          	slli	a5,a4,0x2
40000c8c:	00f50533          	add	a0,a0,a5
40000c90:	00d52023          	sw	a3,0(a0)
40000c94:	06e52c23          	sw	a4,120(a0)
40000c98:	00d52223          	sw	a3,4(a0)
40000c9c:	00c807b3          	add	a5,a6,a2
40000ca0:	00f587b3          	add	a5,a1,a5
40000ca4:	0107a683          	lw	a3,16(a5)
40000ca8:	00e7aa23          	sw	a4,20(a5)
40000cac:	00e7ac23          	sw	a4,24(a5)
40000cb0:	00168713          	addi	a4,a3,1
40000cb4:	00e7a823          	sw	a4,16(a5)
40000cb8:	00052783          	lw	a5,0(a0)
40000cbc:	010585b3          	add	a1,a1,a6
40000cc0:	00c585b3          	add	a1,a1,a2
40000cc4:	00001637          	lui	a2,0x1
40000cc8:	00b605b3          	add	a1,a2,a1
40000ccc:	faf5aa23          	sw	a5,-76(a1)
40000cd0:	00500793          	li	a5,5
40000cd4:	82f1ac23          	sw	a5,-1992(gp) # 40001838 <Int_Glob>
40000cd8:	00008067          	ret

40000cdc <Func_1>:
40000cdc:	0ff57513          	andi	a0,a0,255
40000ce0:	0ff5f593          	andi	a1,a1,255
40000ce4:	00b50663          	beq	a0,a1,40000cf0 <Func_1+0x14>
40000ce8:	00000513          	li	a0,0
40000cec:	00008067          	ret
40000cf0:	82a188a3          	sb	a0,-1999(gp) # 40001831 <Ch_1_Glob>
40000cf4:	00100513          	li	a0,1
40000cf8:	00008067          	ret

40000cfc <Func_2>:
40000cfc:	ff010113          	addi	sp,sp,-16
40000d00:	00812423          	sw	s0,8(sp)
40000d04:	00912223          	sw	s1,4(sp)
40000d08:	00112623          	sw	ra,12(sp)
40000d0c:	00050413          	mv	s0,a0
40000d10:	00058493          	mv	s1,a1
40000d14:	0034c583          	lbu	a1,3(s1)
40000d18:	00244503          	lbu	a0,2(s0)
40000d1c:	fc1ff0ef          	jal	ra,40000cdc <Func_1>
40000d20:	fe051ae3          	bnez	a0,40000d14 <Func_2+0x18>
40000d24:	00048593          	mv	a1,s1
40000d28:	00040513          	mv	a0,s0
40000d2c:	36c000ef          	jal	ra,40001098 <strcmp>
40000d30:	00000793          	li	a5,0
40000d34:	00a05863          	blez	a0,40000d44 <Func_2+0x48>
40000d38:	00a00793          	li	a5,10
40000d3c:	82f1ac23          	sw	a5,-1992(gp) # 40001838 <Int_Glob>
40000d40:	00100793          	li	a5,1
40000d44:	00c12083          	lw	ra,12(sp)
40000d48:	00812403          	lw	s0,8(sp)
40000d4c:	00412483          	lw	s1,4(sp)
40000d50:	00078513          	mv	a0,a5
40000d54:	01010113          	addi	sp,sp,16
40000d58:	00008067          	ret

40000d5c <Func_3>:
40000d5c:	ffe50513          	addi	a0,a0,-2
40000d60:	00153513          	seqz	a0,a0
40000d64:	00008067          	ret

40000d68 <Proc_6>:
40000d68:	ff010113          	addi	sp,sp,-16
40000d6c:	00812423          	sw	s0,8(sp)
40000d70:	00912223          	sw	s1,4(sp)
40000d74:	00050413          	mv	s0,a0
40000d78:	00112623          	sw	ra,12(sp)
40000d7c:	00058493          	mv	s1,a1
40000d80:	fddff0ef          	jal	ra,40000d5c <Func_3>
40000d84:	00040793          	mv	a5,s0
40000d88:	00051463          	bnez	a0,40000d90 <Proc_6+0x28>
40000d8c:	00300793          	li	a5,3
40000d90:	00f4a023          	sw	a5,0(s1)
40000d94:	00100713          	li	a4,1
40000d98:	02e40863          	beq	s0,a4,40000dc8 <Proc_6+0x60>
40000d9c:	02040e63          	beqz	s0,40000dd8 <Proc_6+0x70>
40000da0:	00200793          	li	a5,2
40000da4:	04f40663          	beq	s0,a5,40000df0 <Proc_6+0x88>
40000da8:	00400713          	li	a4,4
40000dac:	00e41463          	bne	s0,a4,40000db4 <Proc_6+0x4c>
40000db0:	00f4a023          	sw	a5,0(s1)
40000db4:	00c12083          	lw	ra,12(sp)
40000db8:	00812403          	lw	s0,8(sp)
40000dbc:	00412483          	lw	s1,4(sp)
40000dc0:	01010113          	addi	sp,sp,16
40000dc4:	00008067          	ret
40000dc8:	83818793          	addi	a5,gp,-1992 # 40001838 <Int_Glob>
40000dcc:	0007a703          	lw	a4,0(a5)
40000dd0:	06400793          	li	a5,100
40000dd4:	02e7da63          	bge	a5,a4,40000e08 <Proc_6+0xa0>
40000dd8:	00c12083          	lw	ra,12(sp)
40000ddc:	00812403          	lw	s0,8(sp)
40000de0:	0004a023          	sw	zero,0(s1)
40000de4:	00412483          	lw	s1,4(sp)
40000de8:	01010113          	addi	sp,sp,16
40000dec:	00008067          	ret
40000df0:	00c12083          	lw	ra,12(sp)
40000df4:	00812403          	lw	s0,8(sp)
40000df8:	00e4a023          	sw	a4,0(s1)
40000dfc:	00412483          	lw	s1,4(sp)
40000e00:	01010113          	addi	sp,sp,16
40000e04:	00008067          	ret
40000e08:	00300793          	li	a5,3
40000e0c:	00f4a023          	sw	a5,0(s1)
40000e10:	fa5ff06f          	j	40000db4 <Proc_6+0x4c>

40000e14 <Proc_2>:
40000e14:	8311c703          	lbu	a4,-1999(gp) # 40001831 <Ch_1_Glob>
40000e18:	04100793          	li	a5,65
40000e1c:	00f70463          	beq	a4,a5,40000e24 <Proc_2+0x10>
40000e20:	00008067          	ret
40000e24:	00052783          	lw	a5,0(a0)
40000e28:	83818713          	addi	a4,gp,-1992 # 40001838 <Int_Glob>
40000e2c:	00072703          	lw	a4,0(a4)
40000e30:	00978793          	addi	a5,a5,9
40000e34:	40e787b3          	sub	a5,a5,a4
40000e38:	00f52023          	sw	a5,0(a0)
40000e3c:	00008067          	ret

40000e40 <Proc_3>:
40000e40:	84018793          	addi	a5,gp,-1984 # 40001840 <Ptr_Glob>
40000e44:	0007a603          	lw	a2,0(a5)
40000e48:	00060863          	beqz	a2,40000e58 <Proc_3+0x18>
40000e4c:	00062703          	lw	a4,0(a2) # 1000 <__stack_size>
40000e50:	00e52023          	sw	a4,0(a0)
40000e54:	0007a603          	lw	a2,0(a5)
40000e58:	83818793          	addi	a5,gp,-1992 # 40001838 <Int_Glob>
40000e5c:	0007a583          	lw	a1,0(a5)
40000e60:	00c60613          	addi	a2,a2,12
40000e64:	00a00513          	li	a0,10
40000e68:	e01ff06f          	j	40000c68 <Proc_7>

40000e6c <Proc_1>:
40000e6c:	ff010113          	addi	sp,sp,-16
40000e70:	01212023          	sw	s2,0(sp)
40000e74:	84018913          	addi	s2,gp,-1984 # 40001840 <Ptr_Glob>
40000e78:	00092783          	lw	a5,0(s2)
40000e7c:	00812423          	sw	s0,8(sp)
40000e80:	00052403          	lw	s0,0(a0)
40000e84:	0007a703          	lw	a4,0(a5)
40000e88:	00912223          	sw	s1,4(sp)
40000e8c:	0047ae83          	lw	t4,4(a5)
40000e90:	0087ae03          	lw	t3,8(a5)
40000e94:	0107a303          	lw	t1,16(a5)
40000e98:	0147a883          	lw	a7,20(a5)
40000e9c:	0187a803          	lw	a6,24(a5)
40000ea0:	0207a583          	lw	a1,32(a5)
40000ea4:	0247a603          	lw	a2,36(a5)
40000ea8:	0287a683          	lw	a3,40(a5)
40000eac:	00112623          	sw	ra,12(sp)
40000eb0:	00050493          	mv	s1,a0
40000eb4:	01c7a503          	lw	a0,28(a5)
40000eb8:	02c7a783          	lw	a5,44(a5)
40000ebc:	00e42023          	sw	a4,0(s0)
40000ec0:	0004a703          	lw	a4,0(s1)
40000ec4:	00a42e23          	sw	a0,28(s0)
40000ec8:	02f42623          	sw	a5,44(s0)
40000ecc:	01d42223          	sw	t4,4(s0)
40000ed0:	00500793          	li	a5,5
40000ed4:	01c42423          	sw	t3,8(s0)
40000ed8:	00642823          	sw	t1,16(s0)
40000edc:	01142a23          	sw	a7,20(s0)
40000ee0:	01042c23          	sw	a6,24(s0)
40000ee4:	02b42023          	sw	a1,32(s0)
40000ee8:	02c42223          	sw	a2,36(s0)
40000eec:	02d42423          	sw	a3,40(s0)
40000ef0:	00f4a623          	sw	a5,12(s1)
40000ef4:	00f42623          	sw	a5,12(s0)
40000ef8:	00e42023          	sw	a4,0(s0)
40000efc:	00040513          	mv	a0,s0
40000f00:	f41ff0ef          	jal	ra,40000e40 <Proc_3>
40000f04:	00442783          	lw	a5,4(s0)
40000f08:	08078063          	beqz	a5,40000f88 <Proc_1+0x11c>
40000f0c:	0004a783          	lw	a5,0(s1)
40000f10:	00c12083          	lw	ra,12(sp)
40000f14:	00812403          	lw	s0,8(sp)
40000f18:	0007af83          	lw	t6,0(a5)
40000f1c:	0047af03          	lw	t5,4(a5)
40000f20:	0087ae83          	lw	t4,8(a5)
40000f24:	00c7ae03          	lw	t3,12(a5)
40000f28:	0107a303          	lw	t1,16(a5)
40000f2c:	0147a883          	lw	a7,20(a5)
40000f30:	0187a803          	lw	a6,24(a5)
40000f34:	01c7a583          	lw	a1,28(a5)
40000f38:	0207a603          	lw	a2,32(a5)
40000f3c:	0247a683          	lw	a3,36(a5)
40000f40:	0287a703          	lw	a4,40(a5)
40000f44:	02c7a783          	lw	a5,44(a5)
40000f48:	01f4a023          	sw	t6,0(s1)
40000f4c:	01e4a223          	sw	t5,4(s1)
40000f50:	01d4a423          	sw	t4,8(s1)
40000f54:	01c4a623          	sw	t3,12(s1)
40000f58:	0064a823          	sw	t1,16(s1)
40000f5c:	0114aa23          	sw	a7,20(s1)
40000f60:	0104ac23          	sw	a6,24(s1)
40000f64:	00b4ae23          	sw	a1,28(s1)
40000f68:	02c4a023          	sw	a2,32(s1)
40000f6c:	02d4a223          	sw	a3,36(s1)
40000f70:	02e4a423          	sw	a4,40(s1)
40000f74:	02f4a623          	sw	a5,44(s1)
40000f78:	00012903          	lw	s2,0(sp)
40000f7c:	00412483          	lw	s1,4(sp)
40000f80:	01010113          	addi	sp,sp,16
40000f84:	00008067          	ret
40000f88:	0084a503          	lw	a0,8(s1)
40000f8c:	00600793          	li	a5,6
40000f90:	00840593          	addi	a1,s0,8
40000f94:	00f42623          	sw	a5,12(s0)
40000f98:	dd1ff0ef          	jal	ra,40000d68 <Proc_6>
40000f9c:	00092783          	lw	a5,0(s2)
40000fa0:	00c42503          	lw	a0,12(s0)
40000fa4:	00c40613          	addi	a2,s0,12
40000fa8:	0007a783          	lw	a5,0(a5)
40000fac:	00c12083          	lw	ra,12(sp)
40000fb0:	00412483          	lw	s1,4(sp)
40000fb4:	00f42023          	sw	a5,0(s0)
40000fb8:	00812403          	lw	s0,8(sp)
40000fbc:	00012903          	lw	s2,0(sp)
40000fc0:	00a00593          	li	a1,10
40000fc4:	01010113          	addi	sp,sp,16
40000fc8:	ca1ff06f          	j	40000c68 <Proc_7>

40000fcc <Proc_4>:
40000fcc:	83418713          	addi	a4,gp,-1996 # 40001834 <Bool_Glob>
40000fd0:	00072683          	lw	a3,0(a4)
40000fd4:	8311c783          	lbu	a5,-1999(gp) # 40001831 <Ch_1_Glob>
40000fd8:	fbf78793          	addi	a5,a5,-65
40000fdc:	0017b793          	seqz	a5,a5
40000fe0:	00d7e7b3          	or	a5,a5,a3
40000fe4:	00f72023          	sw	a5,0(a4)
40000fe8:	04200793          	li	a5,66
40000fec:	82f18823          	sb	a5,-2000(gp) # 40001830 <Ch_2_Glob>
40000ff0:	00008067          	ret

40000ff4 <Proc_5>:
40000ff4:	04100793          	li	a5,65
40000ff8:	82f188a3          	sb	a5,-1999(gp) # 40001831 <Ch_1_Glob>
40000ffc:	8201aa23          	sw	zero,-1996(gp) # 40001834 <Bool_Glob>
40001000:	00008067          	ret

40001004 <__libc_init_array>:
40001004:	ff010113          	addi	sp,sp,-16
40001008:	00812423          	sw	s0,8(sp)
4000100c:	01212023          	sw	s2,0(sp)
40001010:	00000417          	auipc	s0,0x0
40001014:	20440413          	addi	s0,s0,516 # 40001214 <__init_array_end>
40001018:	00000917          	auipc	s2,0x0
4000101c:	1fc90913          	addi	s2,s2,508 # 40001214 <__init_array_end>
40001020:	40890933          	sub	s2,s2,s0
40001024:	00112623          	sw	ra,12(sp)
40001028:	00912223          	sw	s1,4(sp)
4000102c:	40295913          	srai	s2,s2,0x2
40001030:	00090e63          	beqz	s2,4000104c <__libc_init_array+0x48>
40001034:	00000493          	li	s1,0
40001038:	00042783          	lw	a5,0(s0)
4000103c:	00148493          	addi	s1,s1,1
40001040:	00440413          	addi	s0,s0,4
40001044:	000780e7          	jalr	a5
40001048:	fe9918e3          	bne	s2,s1,40001038 <__libc_init_array+0x34>
4000104c:	00000417          	auipc	s0,0x0
40001050:	1c840413          	addi	s0,s0,456 # 40001214 <__init_array_end>
40001054:	00000917          	auipc	s2,0x0
40001058:	1c090913          	addi	s2,s2,448 # 40001214 <__init_array_end>
4000105c:	40890933          	sub	s2,s2,s0
40001060:	40295913          	srai	s2,s2,0x2
40001064:	00090e63          	beqz	s2,40001080 <__libc_init_array+0x7c>
40001068:	00000493          	li	s1,0
4000106c:	00042783          	lw	a5,0(s0)
40001070:	00148493          	addi	s1,s1,1
40001074:	00440413          	addi	s0,s0,4
40001078:	000780e7          	jalr	a5
4000107c:	fe9918e3          	bne	s2,s1,4000106c <__libc_init_array+0x68>
40001080:	00c12083          	lw	ra,12(sp)
40001084:	00812403          	lw	s0,8(sp)
40001088:	00412483          	lw	s1,4(sp)
4000108c:	00012903          	lw	s2,0(sp)
40001090:	01010113          	addi	sp,sp,16
40001094:	00008067          	ret

40001098 <strcmp>:
40001098:	00b56733          	or	a4,a0,a1
4000109c:	fff00393          	li	t2,-1
400010a0:	00377713          	andi	a4,a4,3
400010a4:	10071063          	bnez	a4,400011a4 <strcmp+0x10c>
400010a8:	7f7f87b7          	lui	a5,0x7f7f8
400010ac:	f7f78793          	addi	a5,a5,-129 # 7f7f7f7f <__freertos_irq_stack_top+0x3f7f2f5f>
400010b0:	00052603          	lw	a2,0(a0)
400010b4:	0005a683          	lw	a3,0(a1)
400010b8:	00f672b3          	and	t0,a2,a5
400010bc:	00f66333          	or	t1,a2,a5
400010c0:	00f282b3          	add	t0,t0,a5
400010c4:	0062e2b3          	or	t0,t0,t1
400010c8:	10729263          	bne	t0,t2,400011cc <strcmp+0x134>
400010cc:	08d61663          	bne	a2,a3,40001158 <strcmp+0xc0>
400010d0:	00452603          	lw	a2,4(a0)
400010d4:	0045a683          	lw	a3,4(a1)
400010d8:	00f672b3          	and	t0,a2,a5
400010dc:	00f66333          	or	t1,a2,a5
400010e0:	00f282b3          	add	t0,t0,a5
400010e4:	0062e2b3          	or	t0,t0,t1
400010e8:	0c729e63          	bne	t0,t2,400011c4 <strcmp+0x12c>
400010ec:	06d61663          	bne	a2,a3,40001158 <strcmp+0xc0>
400010f0:	00852603          	lw	a2,8(a0)
400010f4:	0085a683          	lw	a3,8(a1)
400010f8:	00f672b3          	and	t0,a2,a5
400010fc:	00f66333          	or	t1,a2,a5
40001100:	00f282b3          	add	t0,t0,a5
40001104:	0062e2b3          	or	t0,t0,t1
40001108:	0c729863          	bne	t0,t2,400011d8 <strcmp+0x140>
4000110c:	04d61663          	bne	a2,a3,40001158 <strcmp+0xc0>
40001110:	00c52603          	lw	a2,12(a0)
40001114:	00c5a683          	lw	a3,12(a1)
40001118:	00f672b3          	and	t0,a2,a5
4000111c:	00f66333          	or	t1,a2,a5
40001120:	00f282b3          	add	t0,t0,a5
40001124:	0062e2b3          	or	t0,t0,t1
40001128:	0c729263          	bne	t0,t2,400011ec <strcmp+0x154>
4000112c:	02d61663          	bne	a2,a3,40001158 <strcmp+0xc0>
40001130:	01052603          	lw	a2,16(a0)
40001134:	0105a683          	lw	a3,16(a1)
40001138:	00f672b3          	and	t0,a2,a5
4000113c:	00f66333          	or	t1,a2,a5
40001140:	00f282b3          	add	t0,t0,a5
40001144:	0062e2b3          	or	t0,t0,t1
40001148:	0a729c63          	bne	t0,t2,40001200 <strcmp+0x168>
4000114c:	01450513          	addi	a0,a0,20
40001150:	01458593          	addi	a1,a1,20
40001154:	f4d60ee3          	beq	a2,a3,400010b0 <strcmp+0x18>
40001158:	01061713          	slli	a4,a2,0x10
4000115c:	01069793          	slli	a5,a3,0x10
40001160:	00f71e63          	bne	a4,a5,4000117c <strcmp+0xe4>
40001164:	01065713          	srli	a4,a2,0x10
40001168:	0106d793          	srli	a5,a3,0x10
4000116c:	40f70533          	sub	a0,a4,a5
40001170:	0ff57593          	andi	a1,a0,255
40001174:	02059063          	bnez	a1,40001194 <strcmp+0xfc>
40001178:	00008067          	ret
4000117c:	01075713          	srli	a4,a4,0x10
40001180:	0107d793          	srli	a5,a5,0x10
40001184:	40f70533          	sub	a0,a4,a5
40001188:	0ff57593          	andi	a1,a0,255
4000118c:	00059463          	bnez	a1,40001194 <strcmp+0xfc>
40001190:	00008067          	ret
40001194:	0ff77713          	andi	a4,a4,255
40001198:	0ff7f793          	andi	a5,a5,255
4000119c:	40f70533          	sub	a0,a4,a5
400011a0:	00008067          	ret
400011a4:	00054603          	lbu	a2,0(a0)
400011a8:	0005c683          	lbu	a3,0(a1)
400011ac:	00150513          	addi	a0,a0,1
400011b0:	00158593          	addi	a1,a1,1
400011b4:	00d61463          	bne	a2,a3,400011bc <strcmp+0x124>
400011b8:	fe0616e3          	bnez	a2,400011a4 <strcmp+0x10c>
400011bc:	40d60533          	sub	a0,a2,a3
400011c0:	00008067          	ret
400011c4:	00450513          	addi	a0,a0,4
400011c8:	00458593          	addi	a1,a1,4
400011cc:	fcd61ce3          	bne	a2,a3,400011a4 <strcmp+0x10c>
400011d0:	00000513          	li	a0,0
400011d4:	00008067          	ret
400011d8:	00850513          	addi	a0,a0,8
400011dc:	00858593          	addi	a1,a1,8
400011e0:	fcd612e3          	bne	a2,a3,400011a4 <strcmp+0x10c>
400011e4:	00000513          	li	a0,0
400011e8:	00008067          	ret
400011ec:	00c50513          	addi	a0,a0,12
400011f0:	00c58593          	addi	a1,a1,12
400011f4:	fad618e3          	bne	a2,a3,400011a4 <strcmp+0x10c>
400011f8:	00000513          	li	a0,0
400011fc:	00008067          	ret
40001200:	01050513          	addi	a0,a0,16
40001204:	01058593          	addi	a1,a1,16
40001208:	f8d61ee3          	bne	a2,a3,400011a4 <strcmp+0x10c>
4000120c:	00000513          	li	a0,0
40001210:	00008067          	ret

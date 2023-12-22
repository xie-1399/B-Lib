// Generator : SpinalHDL v1.9.4    git head : 270018552577f3bb8e5339ee2583c9c22d324215
// Component : boxtest
// Git hash  : bd65e416e07ef6caf34ca9075f03de3e522f4ea4

`timescale 1ns/1ps

module boxtest (
  input  wire [31:0]   a,
  input  wire [31:0]   b,
  input  wire          cin,
  output wire [31:0]   c,
  output wire          cout,
  input  wire          clk
);

  wire       [31:0]   blackBox_c;
  wire                blackBox_cout;

  aaaa blackBox (
    .clk  (clk             ), //i
    .a    (a[31:0]         ), //i
    .b    (b[31:0]         ), //i
    .cin  (cin             ), //i
    .c    (blackBox_c[31:0]), //o
    .cout (blackBox_cout   )  //o
  );
  assign c = blackBox_c;
  assign cout = blackBox_cout;

endmodule

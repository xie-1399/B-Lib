// Generator : SpinalHDL v1.9.3    git head : 029104c77a54c53f1edda327a3bea333f7d65fd9
// Component : boxtest
// Git hash  : 17accb0942e31c621b273b3085c1ec008c150a27

`timescale 1ns/1ps

module boxtest (
  input      [31:0]   a,
  input      [31:0]   b,
  input               cin,
  output     [31:0]   c,
  output              cout,
  input               clk
);

  wire       [31:0]   blackBox_c;
  wire                blackBox_cout;

  InlineBlackBoxAdder blackBox (
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

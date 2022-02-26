{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };
        clojure-latest-jdk =
          pkgs.callPackage "${nixpkgs}/pkgs/development/interpreters/clojure" {
            jdk = pkgs.jdk;
          };
      in {
        devShell = pkgs.mkShell {
          buildInputs = with pkgs; [
            clojure-latest-jdk
            nodePackages.npm
            clj-kondo
            clojure-lsp
          ];
        };
      });
}

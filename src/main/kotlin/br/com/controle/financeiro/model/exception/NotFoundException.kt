package br.com.controle.financeiro.model.exception

open class NotFoundException : RuntimeException {
    constructor(id: Long) : super("Could not find resource with$id") {}
    constructor(message: String?) : super(message) {}
}

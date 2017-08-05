# base16-builder-clojure

A builder of [base16](https://github.com/chriskempson/base16) templates.

## Usage

### Building all templates
    $ lein run update
    $ lein run build

### Building a single template
    $ lein run build --template-name [NAME] --template-dir [DIRECTORY]

## Partials
For templates that have complex colour formats, partials can help to simplify
this repetition.

In order to avoid needing to create a partial for each colour, colours can be used
as sections, bringing the correct colour into scope for the partial. For example,
a `json-rgb` partial:

```mustache
{
  "r": {{rgb-r}},
  "g": {{rgb-g}},
  "b": {{rgb-b}}
}
```

can be used as follows:

```mustache
{{#base00}}{{>json-rgb}}{{/base00}}
```

Note that individual colours are not specified in the partial.

Inside partials, the following keys are available:
* `hex`
* `hex-r`
* `hex-g`
* `hex-b`
* `rgb-r`
* `rgb-g`
* `rgb-b`
* `dec-r`
* `dec-g`
* `dec-b`

Partial files can be placed in the `templates` directory of a template.
Partial names must be listed under the partials key of the template's `config.yaml` file.

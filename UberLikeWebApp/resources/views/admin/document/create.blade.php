@extends('admin.layout.base')

@section('title', 'Add Document ')

@section('content')

<div class="content-area py-1">
    <div class="container-fluid">
        <div class="box box-block bg-white">
            <a href="{{ route('admin.document.index') }}" class="btn btn-default pull-right"><i class="fa fa-angle-left"></i> Back</a>

            <h5 style="margin-bottom: 2em;">Add Document</h5>

            <form class="form-horizontal" action="{{route('admin.document.store')}}" method="POST" enctype="multipart/form-data" role="form">
                {{csrf_field()}}
                <div class="form-group row">
                    <label for="name" class="col-xs-12 col-form-label">Document Name</label>
                    <div class="col-xs-10">
                        <input class="form-control" type="text" value="{{ old('name') }}" name="name" required id="name" placeholder="Document Name">
                    </div>
                </div>

                <div class="form-group row">
                    <label for="name" class="col-xs-12 col-form-label">Document Type</label>
                    <div class="col-xs-10">
                        <select name="type">
                            <option value="DRIVER">Driver Review</option>
                            <option value="VEHICLE">Vehicle Review</option>
                        </select>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="zipcode" class="col-xs-12 col-form-label"></label>
                    <div class="col-xs-10">
                        <button type="submit" class="btn btn-primary">Add Document</button>
                        <a href="{{route('admin.document.index')}}" class="btn btn-default">Cancel</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

@endsection
